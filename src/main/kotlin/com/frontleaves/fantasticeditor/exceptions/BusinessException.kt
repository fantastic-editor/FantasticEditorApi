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

package com.frontleaves.fantasticeditor.exceptions

import com.frontleaves.fantasticeditor.utility.ErrorCode

/**
 * # 业务异常类
 * 用于处理业务异常
 *
 * @since v1.0.0
 * @property errorMessage 业务返回自定义报错消息
 * @property errorCode 业务返回报错状态码
 * @property data 业务返回报错数据
 * @constructor 创建一个业务异常，包含数据
 */
class BusinessException(
    val errorMessage: String,
    val errorCode: ErrorCode,
    val data: Any?,
) : RuntimeException(errorMessage) {
    /**
     * # 创建一个业务异常
     *
     * @param message 业务返回自定义报错消息
     * @param errorCode 业务返回报错状态码
     * @constructor 创建一个业务异常，不包含数据
     */
    constructor(message: String, errorCode: ErrorCode) : this(message, errorCode, null)
}
