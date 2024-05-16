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

package com.frontleaves.fantasticeditor.utility

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.crypto.bcrypt.BCrypt
import java.util.*

/**
 * # 基础工具类
 * 用于定义系统中的基础工具类
 *
 * @since v1.0.0
 * @constructor 创建一个基础工具类
 * @author xiao_lfeng | xiangZr-hhh | DC_DC
 */
object Util {
    /**
     * ## Object 转换为 Map
     * 通过反射获取对象的属性和值，将其转换为 Map
     *
     * @param obj 对象
     * @return Map
     */
    @Throws(IllegalAccessException::class)
    fun objectToMap(obj: Any): Map<String, Any> {
        val map: MutableMap<String, Any> = HashMap()
        val clazz: Class<*> = obj.javaClass
        for (field in clazz.declaredFields) {
            field.isAccessible = true
            map[field.name] = field[obj]
        }
        return map
    }

    /**
     * ## 通过字符串生成 UUID
     * 通过字符串生成一个 UUID
     *
     * @param name 字符串
     * @return UUID 对象
     */
    fun makeUUIDByString(name: String): UUID {
        return UUID.nameUUIDFromBytes(name.toByteArray())
    }

    /**
     * ## 生成 UUID
     * 生成一个随机的 UUID
     *
     * @return UUID 对象
     */
    fun makeUUID(): UUID {
        return UUID.randomUUID()
    }

    /**
     * ## 通过字符串 UUID 生成 UUID
     * 通过字符串 UUID 生成一个 UUID
     *
     * @param uuid 字符串 UUID
     * @return UUID 对象
     */
    fun makeUUIDByStringUUID(uuid: String): UUID {
        return UUID.fromString(uuid)
    }

    /**
     * ## 将字符串转换为 Base64 编码
     * 用于将字符串转换为 Base64 编码, 该方法将会返回一个 Base64 编码后的字符串
     *
     * @param str
     * @return
     */
    fun makeStringToBase64(str: String): String {
        return Base64.getEncoder().encodeToString(str.toByteArray())
    }

    /**
     * ## 生成密码
     * 生成密码，先将密码转换为base64编码，再使用BCrypt加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    fun encryptPassword(password: String): String {
        val base64Password = makeStringToBase64(password)
        return BCrypt.hashpw(base64Password, BCrypt.gensalt())
    }

    /**
     * ## 验证密码
     * 验证密码，先将密码转换为base64编码，再使用BCrypt验证
     *
     * @param password 密码
     * @param hashedPassword 加密后的密码
     * @return 是否匹配
     */
    fun verifyPassword(password: String, hashedPassword: String): Boolean {
        val base64Password = makeStringToBase64(password)
        return BCrypt.checkpw(base64Password, hashedPassword)
    }

    /**
     * ## 获取授权 Token
     * 从请求头中获取授权 Token
     *
     * @param request 请求
     * @return Token
     */
    fun getAuthorizationToken(request: HttpServletRequest): String? {
        return takeIf { !request.getHeader("Authorization").isNullOrBlank() }
            ?.let { request.getHeader("Authorization") }
            ?: run { null }
    }

    /**
     * ## 获取用户 UUID
     * 从请求头中获取用户 UUID
     *
     * @param request 请求
     * @return 用户 UUID
     */
    fun getHeaderUUID(request: HttpServletRequest): String? {
        return takeIf { !request.getHeader("X-USER-UUID").isNullOrBlank() }
            ?.let { request.getHeader("X-USER-UUID") }
            ?: run { null }
    }

    /**
     * ## Map 转换为 Object
     * 通过反射获取对象的属性和值，将其转换为 Map
     *
     * @param map Map
     * @param obj 对象
     * @return 对象
     */
    fun <V : Any> mapToObject(map: Map<String, *>, obj: Class<V>): V {
        val instance = obj.getDeclaredConstructor().newInstance()
        for (field in obj.declaredFields) {
            field.isAccessible = true
            if (map.containsKey(field.name)) {
                field.set(instance, map[field.name])
            }
        }
        return instance
    }

    /**
     * ## 生成随机数字
     * 根据 `size` 大小，生成指定大小的随机数
     *
     * @param size 位数
     * @return 随机数
     */
    fun generateNumber(size: Int): String {
        val numberBuild = StringBuilder()
        var number = 0
        while (number++ < size) {
            numberBuild.append(Random().apply { setSeed(System.currentTimeMillis()) }.nextInt(10))
        }
        return numberBuild.toString()
    }

    /**
     * ## 复制对象属性
     * 复制对象属性
     *
     * @param source 源对象
     * @param target 目标对象
     */
    fun <T : Any, R : Any> copyProperties(source: T, target: Class<R>): R {
        val sourceClass = source::class.java
        for (sourceField in sourceClass.declaredFields) {
            try {
                sourceField.isAccessible = true
                val targetField = target.getDeclaredField(sourceField.name)
                targetField.isAccessible = true
                targetField.set(target, sourceField.get(source))
            } catch (e: NoSuchFieldException) {
                // Ignore if the target object does not have the field
            }
        }
        return target.getDeclaredConstructor().newInstance()
    }
}
