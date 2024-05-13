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
