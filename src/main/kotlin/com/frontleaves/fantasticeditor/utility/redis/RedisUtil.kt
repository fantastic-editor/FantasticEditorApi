/*
 * *******************************************************************************
 * Copyright (C) 2024-NOW(至今) 妙笔智编
 * Author: 锋楪技术团队
 *
 * 本文件包含 妙笔智编「FantasticEditor」 的源代码，该项目的所有源代码均遵循MIT开源许可证协议。
 * 本代码仅允许在十三届软件杯比赛授权比赛方可直接使用
 * *******************************************************************************
 * 免责声明：
 * 使用本软件的风险由用户自担。作者或版权持有人在法律允许的最大范围内，
 * 对因使用本软件内容而导致的任何直接或间接的损失不承担任何责任。
 * *******************************************************************************
 */

package com.frontleaves.fantasticeditor.utility.redis

import com.frontleaves.fantasticeditor.annotations.KSlf4j.Companion.log
import com.frontleaves.fantasticeditor.exceptions.ServerInternalErrorException
import com.google.gson.Gson
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.lang.NonNull
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * # Redis 工具
 * 用于定义 Redis 工具
 *
 * @property redisTemplate Redis 模板
 * @constructor 创建一个 Redis 工具
 * @since v1.0.0
 * @author xiao_lfeng
 */
@Component
class RedisUtil(
    private val redisTemplate: RedisTemplate<String, Any>,
    private val gson: Gson,
) {
    // ************************************ Redis Common ************************************

    /**
     * ## 指定缓存失效时间
     * 设置 key 的过期时间, 单位为秒
     *
     * @param key  键
     * @param time 时间(秒)
     * @return 是否设置成功
     */
    fun expire(
        @NonNull key: String?,
        time: Long,
    ): Boolean {
        return try {
            takeIf { time > 0 }?.let {
                redisTemplate.expire(key!!, time, TimeUnit.SECONDS)
                true
            } ?: run {
                log.warn("[REDIS] <Func:expire> 时间设置错误，时间 [{}] 无法设置", time)
                false
            }
        } catch (e: NullPointerException) {
            log.warn("[REDIS] <Func:expire> 键值对为空")
            false
        }
    }

    /**
     * ## 根据 key 获取过期时间
     * 获取的时间为有效期倒计时，单位为秒，返回 0 代表永久有效
     *
     * @param key 键 不能为 null
     * @return 时间(秒) 返回 0 代表永久有效
     */
    fun getExpire(key: String): Long {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS)
    }

    /**
     * ## 判断 key 是否存在
     * 判断 key 是否存在, true 存在, false 不存在
     *
     * @param key 键
     * @return 是否存在
     */
    fun hasKey(key: String): Boolean {
        try {
            return java.lang.Boolean.TRUE == redisTemplate.hasKey(key)
        } catch (e: NullPointerException) {
            log.warn("[REDIS] <Func:hasKey> 键值对为空")
            return false
        }
    }

    /**
     * ## 删除缓存
     * 根据 key 删除缓存, 可以传一个值或多个; 返回是否成功
     *
     * @param key 可以传一个值 或多个
     * @return 是否成功
     */
    fun delete(vararg key: String): Boolean {
        return if (key.isNotEmpty()) {
            listOf(*key).forEach {
                if (java.lang.Boolean.TRUE != redisTemplate.delete(it)) {
                    log.warn("[REDIS] <Func:delete> 删除失败，键 [{}] 不存在", it)
                }
            }
            true
        } else {
            log.warn("[REDIS] <Func:delete> 传入参数 {} 键值对为空", key.javaClass.name)
            false
        }
    }

    /**
     * ## 删除缓存
     * 根据 key 删除缓存, 可以传一个值或多个; 返回是否成功; 传入参数为 List
     *
     * @param key 传入List进行删除
     * @return 是否成功
     */
    fun deleteList(key: List<String>): Boolean {
        return if (key.isNotEmpty()) {
            key.forEach {
                if (java.lang.Boolean.TRUE != redisTemplate.delete(it)) {
                    log.warn("[REDIS] <Func:deleteList> 删除失败，键 [{}] 不存在", it)
                }
            }
            true
        } else {
            log.warn("[REDIS] <Func:deleteList> 传入参数 {} 键值对为空", key.javaClass.name)
            false
        }
    }

    // ************************************ Redis String ************************************

    /**
     * ## 普通缓存获取
     * 根据 key 获取值, 返回 Object
     *
     * @param key 键
     * @return 值
     */
    fun get(key: String): Any? {
        return redisTemplate.opsForValue()[key]
    }

    /**
     * ## 普通缓存放入
     * 根据 key 存入键值对、value 存入值, 返回是否成功；不限制时间
     *
     * @param key   键
     * @param value 值
     * @return 是否成功
     */
    fun set(
        key: String,
        value: Any,
    ): Boolean {
        return try {
            redisTemplate.opsForValue()[key] = value
            true
        } catch (e: NullPointerException) {
            log.warn("[REDIS] <Func:set> 键值对为空")
            false
        }
    }

    /**
     * ## 普通缓存放入并设置时间
     * 根据 key 存入键值对、value 存入值、time 存入时间, 返回是否成功
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return 是否成功
     */
    fun set(
        key: String,
        value: Any,
        time: Long,
    ): Boolean {
        return try {
            if (time > 0) {
                redisTemplate.opsForValue()[key, value, time] = TimeUnit.SECONDS
                true
            } else {
                log.warn("[REDIS] <Func:set|Time> 时间设置错误，时间 [{}] 无法设置", time)
                false
            }
        } catch (e: NullPointerException) {
            log.warn("[REDIS] <Func:set|Time> 键值对为空")
            false
        }
    }

    /**
     * ## 对数据进行递增操作
     * 如果 key 不存在，那么 key 的值会先被初始化为 0，然后再执行 INCR 操作; 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回
     * 一个错误
     *
     * @param key   键
     * @param delta 递增因子
     * @return 递增后的值
     */
    @Throws(ServerInternalErrorException::class)
    fun increment(
        key: String,
        delta: Long,
    ): Long {
        // 如果出现不是数字不可递增则会抛出异常
        // TODO-RedisUtil[2024042601] 若 key 为非数字类型，会抛出异常，该异常需要进行测试获取
        if (delta > 0) {
            return redisTemplate.opsForValue().increment(key, delta) ?: 0
        } else {
            throw ServerInternalErrorException("递增因子不能为负数 [$delta]")
        }
    }

    /**
     * ## 对数据进行递减操作
     * 如果 key 不存在，那么 key 的值会先被初始化为 0，然后再执行 DECR 操作; 如果值包含错误的类型，或字符串类型的值不能表示为数字，那么返回
     * 一个错误
     *
     * @param key   键
     * @param delta 递减因子
     * @return 递减后的值
     */
    @Throws(ServerInternalErrorException::class)
    fun decrement(
        key: String,
        delta: Long,
    ): Long {
        // 如果出现不是数字不可递减则会抛出异常
        // TODO-RedisUtil[2024042602] 若 key 为非数字类型，会抛出异常，该异常需要进行测试获取
        if (delta > 0) {
            return redisTemplate.opsForValue().decrement(key, -delta) ?: 0
        } else {
            throw ServerInternalErrorException("递增因子不能为负数 [$delta]")
        }
    }

    // ************************************ Redis Hash ************************************

    /**
     * ## HashGet
     * 获取 key 对应的哈希表中给定域的值
     *
     * @param key       键
     * @param <V>       期望获取的单例数据类型
     * @return 值
     */
    fun hashGet(
        key: String,
    ): Map<String, Any>? {
        val getResult = redisTemplate.opsForHash<String, Any>().entries(key)
        return takeIf { getResult.isNotEmpty() }?.let { getResult } ?: run {
            log.warn("[REDIS] <Func:hashGet> 数据 $key 为空")
            null
        }
    }

    /**
     * ## HashGetField
     * 获取 key 对应哈希表中给定域的值
     *
     * @param key       键
     * @param hashField 项
     * @return 值
     */
    fun hashGetField(
        key: String,
        hashField: String,
    ): Any? {
        return redisTemplate.opsForHash<Any, Any>()[key, hashField]
    }

    /**
     * ## HashGetAll
     * 获取 key 对应哈希表中的全部值，返回一个列表；若 key 不存在，返回一个空列表；若 key 不是哈希表，返回一个错误；
     * 若 `clazz` 与实际类型不匹配，将会返回 null
     *
     * @param key   键
     * @param clazz 期望获取的数据类型
     * @param <V>   期望获取的单例数据类型
     * @return 值
     </V> */
    fun <V : Any> hashGetAll(
        key: String,
        clazz: Class<V>,
    ): List<V>? {
        val getResult = redisTemplate.opsForHash<Any, Any>().values(key)
        if (getResult.isNotEmpty()) {
            return getResult.stream().filter { clazz.isInstance(it) }.map { clazz.cast(it) }.toList()
        } else {
            log.warn("[REDIS] <Func:hashGetAll> 数据为空")
            return null
        }
    }

    /**
     * ## HashPutField
     * 设置 key 对应哈希表中给定域的值
     *
     * @param key       键
     * @param hashField 项
     * @param value     值
     * @return 是否成功
     */
    fun hashPutField(
        key: String,
        hashField: String,
        value: Any,
    ): Boolean {
        return try {
            redisTemplate.opsForHash<Any, Any>().put(key, hashField, value)
            true
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:hashPutField> 数据插入失败")
            false
        }
    }

    /**
     * ## HashPut
     * 设置 key 对应哈希表中给定域 `field` 的值，如果 key 和 field 不存在，一个新的哈希表被创建并进行创建操作
     *
     * @param key   键
     * @param field 项
     * @param value 值
     * @return 是否成功
     */
    fun <V : Any> hashPut(
        key: String,
        field: String,
        value: V,
    ): Boolean {
        try {
            redisTemplate.opsForHash<Any, Any>().put(key, field, value)
            return true
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:hashPut> 数据插入失败 | {}", e.message, e)
            return false
        }
    }

    /**
     * ## HashSet
     * 设定一个哈希表，需要传入 key 以及 `V` 对象；
     *
     * @param key   键
     * @param value 值
     * @param <V>   期望设置的数据类型
     * @return 是否成功
     </V> */
    fun <V : Any> hashSet(
        key: String,
        value: V,
    ): Boolean {
        try {
            val getHash = gson.fromJson<HashMap<String, String?>>(gson.toJson(value), HashMap::class.java)
            redisTemplate.opsForHash<Any, Any>().putAll(key, getHash)
            return true
        } catch (e: IllegalAccessException) {
            log.warn("[REDIS] <Func:hashSet> 插入对象 [{}] 无法转换为 Map 对象", value.javaClass.getName())
            return false
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:hashSet> 数据插入失败 | {}", e.message, e)
            return false
        }
    }

    /**
     * ## HashSet
     * 设定一个哈希表，需要传入 key 以及 `V` 对象，同时设置过期时间；
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @param <V>   期望设置的数据类型
     * @return 是否成功
     </V> */
    fun <V : Any> hashSet(
        key: String,
        value: V,
        time: Long,
    ): Boolean {
        return try {
            val getHash = gson.fromJson<HashMap<String, String?>>(gson.toJson(value), HashMap::class.java)
            redisTemplate.opsForHash<Any, Any>().putAll(key, getHash)
            if (time > 0) {
                expire(key, time)
            } else {
                log.warn("[REDIS] <Func:hashSet|Time> 时间设置错误，时间 [{}] 无法设置", time)
                false
            }
        } catch (e: IllegalAccessException) {
            log.warn("[REDIS] <Func:hashSet|Time> 插入对象 [{}] 无法转换为 Map 对象", value.javaClass.getName())
            false
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:hashSet|Time> 数据插入失败 | {}", e.message)
            false
        }
    }

    /**
     * ## HashDelete
     * 删除 key 对应哈希表中给定域的值
     *
     * @param key   键
     * @param field 项
     * @return 是否成功
     */
    fun hashDelete(
        key: String,
        field: String,
    ): Boolean {
        try {
            redisTemplate.opsForHash<Any, Any>().delete(key, field)
            return true
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:hashDelete> 数据删除失败")
            return false
        }
    }

    /**
     * ## HashIncrement
     * 为哈希表 key 中的域 field 的值加上增量 delta
     *
     * @param key   键
     * @param field 项
     * @param delta 递增因子
     * @return 递增后的值
     */
    fun hashIncrement(
        key: String,
        field: String,
        delta: Long,
    ): Long {
        // TODO-RedisUtil[2024042603] 若 key-field 为非数字类型，会抛出异常，该异常需要进行测试获取
        if (delta > 0) {
            return redisTemplate.opsForHash<Any, Any>().increment(key, field, delta)
        } else {
            throw ServerInternalErrorException("递增因子不能为负数 [$delta]")
        }
    }

    /**
     * ## HashDecrement
     * 为哈希表 key 中的域 field 的值减去减量 delta
     *
     * @param key   键
     * @param field 项
     * @param delta 递减因子
     * @return 递减后的值
     */
    fun hashDecrement(
        key: String,
        field: String,
        delta: Long,
    ): Long {
        // TODO-RedisUtil[2024042604] 若 key-field 为非数字类型，会抛出异常，该异常需要进行测试获取
        if (delta > 0) {
            return redisTemplate.opsForHash<Any, Any>().increment(key, field, -delta)
        } else {
            throw ServerInternalErrorException("递增因子不能为负数 [$delta]")
        }
    }

    // ************************************ Redis Set ************************************

    /**
     * ## SetGet
     * 获取 key 对应的集合中的所有元素
     *
     * @param key 键
     */
    fun setGet(key: String): Set<Any>? {
        return try {
            redisTemplate.opsForSet().members(key)
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:setGet> 数据获取失败")
            null
        }
    }

    /**
     * ## setHasKey
     * 根据 value 从一个 set 中查询数据; 如果 value 存在，返回 true，否则返回 false
     *
     * @param key    键
     * @param values 值
     * @param <V>    期望添加的数据类型
     * @return 是否成功
     </V> */
    fun <V : Any> setHasKey(
        key: String,
        values: V,
    ): Boolean {
        return try {
            val isMember = redisTemplate.opsForSet().isMember(key, values) ?: false
            isMember
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:setPut> 数据插入失败")
            false
        }
    }

    /**
     * ## SetAdd
     * 向 key 放入 values 数据；将数据放入缓存中进行查询
     *
     * @param key    键
     * @param values 值
     * @return 成功个数
     */
    fun setAdd(
        key: String,
        vararg values: Any,
    ): Long {
        try {
            if (values.isNotEmpty()) {
                return redisTemplate.opsForSet().add(key, *values) ?: 0
            }
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:setAdd> 数据插入失败")
        }
        return 0L
    }

    /**
     * ## SetAdd
     * 向 key 放入 values 数据；将数据放入缓存中进行查询, 并设置过期时间;
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值
     * @return 成功个数
     */
    fun setAdd(
        time: Long,
        key: String,
        vararg values: Any,
    ): Long {
        try {
            if (time > 0) {
                if (values.isNotEmpty()) {
                    val addResult = redisTemplate.opsForSet().add(key, *values) ?: 0
                    this.expire(key, time)
                    return addResult
                }
            } else {
                log.warn("[REDIS] <Func:setAdd|Time> 时间设置错误，时间 [{}] 无法设置", time)
            }
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:setAdd|Time> 数据插入失败")
        }
        return 0L
    }

    /**
     * ## SetSize
     * 获取 key 对应的集合中的元素个数, 如果 key 不存在，返回 0
     *
     * @param key 键
     * @return 元素个数
     */
    fun setSize(key: String): Long {
        try {
            return redisTemplate.opsForSet().size(key) ?: 0
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:setSize> 数据获取失败")
            return 0L
        }
    }

    /**
     * ## SetRemove
     * 从 key 对应的集合中删除 values 数据
     *
     * @param key    键
     * @param values 值
     * @return 成功个数
     */
    fun setRemove(
        key: String,
        vararg values: Any,
    ): Long {
        try {
            if (values.isNotEmpty()) {
                return redisTemplate.opsForSet().remove(key, values) ?: 0
            }
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:setRemove> 数据删除失败")
        }
        return 0L
    }

    // ************************************ Redis List ************************************

    /**
     * ## ListRange
     * 获取 key 对应的列表中的所有元素
     *
     * @param key 键
     * @return 值
     */
    fun listRange(key: String): List<Any>? {
        try {
            return redisTemplate.opsForList().range(key, 0, -1)
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:listRange> 数据获取失败")
            return null
        }
    }

    /**
     * ## ListRange
     * 获取 key 对应的列表中指定范围的元素
     *
     * @param key   键
     * @param start 起始位置
     * @param end   结束位置
     * @return 值
     */
    fun listRange(
        key: String,
        start: Long,
        end: Long,
    ): List<Any>? {
        try {
            return redisTemplate.opsForList().range(key, start, end)
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:listRange|limits> 数据获取失败")
            return null
        }
    }

    /**
     * ## ListIndex
     * 通过索引获取列表中的元素
     *
     * @param key   键
     * @param index 索引
     * @return 值
     */
    fun listIndex(
        key: String,
        index: Long,
    ): Any? {
        try {
            return redisTemplate.opsForList().index(key, index)
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:listIndex> 数据获取失败")
            return null
        }
    }

    /**
     * ## ListSize
     * 获取 key 对应的列表中的元素个数
     *
     * @param key 键
     * @return 元素个数
     */
    fun listSize(key: String): Long {
        try {
            return redisTemplate.opsForList().size(key) ?: 0
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:listSize> 数据获取失败")
            return 0L
        }
    }

    /**
     * ## ListLeftPush
     * 将一个或多个值插入到列表头部
     *
     * @param key    键
     * @param values 值
     * @return 元素个数
     */

    fun listLeftPush(
        key: String,
        vararg values: Any,
    ): Long {
        try {
            if (values.isNotEmpty()) {
                return redisTemplate.opsForList().leftPushAll(key, *values) ?: 0L
            }
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:listLeftPush> 数据插入失败")
        }
        return 0L
    }

    /**
     * ## ListRightPush
     * 将一个或多个值插入到列表尾部
     *
     * @param key    键
     * @param values 值
     * @return 元素个数
     */
    fun listRightPush(
        key: String,
        vararg values: Any,
    ): Long {
        try {
            if (values.isNotEmpty()) {
                return redisTemplate.opsForList().rightPushAll(key, *values) ?: 0L
            }
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:listRightPush> 数据插入失败")
        }
        return 0L
    }

    /**
     * ## ListRemoveValue
     * 从列表中删除元素, 删除个数(0: 删除所有; >0: 从头部删除指定个数; <0: 从尾部删除指定个数); 返回删除个数
     *
     * @param key   键
     * @param count 删除个数
     * @param value 值(删除的元素)
     * @return 删除个数
     */
    fun listRemoveValue(
        key: String,
        count: Long,
        value: Any,
    ): Long {
        try {
            return redisTemplate.opsForList().remove(key!!, count, value!!) ?: 0L
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:listRemoveValue> 数据删除失败")
            return 0L
        }
    }

    /**
     * ## ListTrim
     * 截取列表指定范围内的元素
     *
     * @param key   键
     * @param start 起始位置
     * @param end   结束位置
     * @return 是否成功
     */
    fun listTrim(
        key: String,
        start: Long,
        end: Long,
    ): Boolean {
        try {
            redisTemplate.opsForList().trim(key, start, end)
            return true
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:listTrim> 数据截取失败")
            return false
        }
    }

    /**
     * ## ListSet
     * 通过索引设置列表元素的值
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return 是否成功
     */
    fun listSet(
        key: String,
        index: Long,
        value: Any,
    ): Boolean {
        try {
            redisTemplate.opsForList()[key, index] = value
            return true
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:listSet> 数据设置失败")
            return false
        }
    }

    /**
     * ## ListRemove
     * 通过索引删除列表中的元素
     *
     * @param key   键
     * @param index 索引
     * @return 值
     */
    fun listRemove(
        key: String,
        index: Long,
    ): Any? {
        try {
            return redisTemplate.opsForList().index(key, index)
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:listRemove> 数据删除失败")
            return null
        }
    }

    /**
     * ## ListLeftPop
     * 移出并获取列表的第一个元素
     *
     * @param key 键
     * @return 值
     */
    fun listPop(key: String): Any? {
        try {
            return redisTemplate.opsForList().leftPop(key)
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:listLeftPop> 数据获取失败")
            return null
        }
    }

    /**
     * ## ListRightPop
     * 移出并获取列表的最后一个元素
     *
     * @param key 键
     * @return 值
     */
    fun listRightPop(key: String): Any? {
        try {
            return redisTemplate.opsForList().rightPop(key)
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:listRightPop> 数据获取失败")
            return null
        }
    }

    /**
     * ## ListRightPopAndLeftPush
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     *
     * @param sourceKey      源键
     * @param destinationKey 目标键
     * @return 值
     */
    fun listRightPopAndLeftPush(
        sourceKey: String,
        destinationKey: String,
    ): Any? {
        try {
            return redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey)
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:listRightPopAndLeftPush> 数据获取失败")
            return null
        }
    }

    /**
     * ## ListRightPopAndLeftPush
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     *
     * @param sourceKey      源键
     * @param destinationKey 目标键
     * @param time           时间(秒)
     * @return 值
     */
    fun listRightPopAndLeftPush(
        sourceKey: String,
        destinationKey: String,
        time: Long,
    ): Any? {
        try {
            if (time > 0) {
                val value = redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey)
                expire(destinationKey, time)
                return value
            } else {
                log.warn("[REDIS] <Func:listRightPopAndLeftPush|Time> 时间设置错误，时间 [{}] 无法设置", time)
                return null
            }
        } catch (e: Exception) {
            log.warn("[REDIS] <Func:listRightPopAndLeftPush|Time> 数据获取失败")
            return null
        }
    }
}
