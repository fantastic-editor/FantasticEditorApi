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

import java.util.*

/**
 * # 基础工具类
 * 用于定义系统中的基础工具类
 *
 * @since v1.0.0
 * @constructor 创建一个基础工具类
 */
object BasicUtil {
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
    fun makeUuidByString(name: String?): UUID {
        return UUID.fromString(name)
    }

    /**
     * ## 生成 UUID
     * 生成一个随机的 UUID
     *
     * @return UUID 对象
     */
    fun makeUuid(): UUID {
        return UUID.randomUUID()
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
}
