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

package com.frontleaves.fantasticeditor.exceptions.library

/**
 * # 系统参数异常
 * 用于定义系统参数异常；这个异常一般是开发者在编写代码时，由于参数传递错误导致的异常
 *
 * @since v1.0.0
 * @see RuntimeException
 * @property message 异常信息
 * @property data 异常数据
 * @constructor 创建一个系统参数异常
 * @author xiao_lfeng
 */
class SystemParameterErrorException(override val message: String, private val data: Any?) : RuntimeException(message) {
    constructor(message: String) : this(message, null)
}
