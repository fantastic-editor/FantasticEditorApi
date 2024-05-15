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

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * # 基础响应类
 * 用于定义一个基础的响应类，用于定义一个基础的响应类
 *
 * @since v1.0.0
 * @property output 输出的信息
 * @property code 输出的状态码
 * @property message 输出的消息
 * @property errorMessage 输出的错误消息
 * @property data 输出的数据
 * @constructor 创建一个基础响应类
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class BaseResponse<E>(
    val output: String,
    val code: Int,
    val message: String,
    val errorMessage: String?,
    val data: E?,
)
