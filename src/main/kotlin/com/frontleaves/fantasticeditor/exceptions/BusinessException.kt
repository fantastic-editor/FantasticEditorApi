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
