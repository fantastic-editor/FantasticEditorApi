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

import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError

/**
 * # 请求路径参数异常
 * 用于定义请求路径参数异常；
 *
 * @since v1.0.0
 * @see RuntimeException
 * @property message 异常信息
 * @property bindingResult 请求路径参数绑定结果
 * @constructor 创建一个请求路径参数异常
 * @author xiao_lfeng
 */
class RequestBodyParametersException(
    override val message: String,
    private val bindingResult: BindingResult,
) : RuntimeException(message) {
    /**
     * # 错误信息
     * 用于定义请求路径参数异常的错误信息
     *
     * @since v1.0.0
     * @property field 字段
     * @property message 消息
     * @property value 值
     * @property code 代码
     * @constructor 创建一个错误信息
     */
    class ErrorFunc(val field: String, val message: String, val value: String, val code: String)

    var allErrors: List<ObjectError> = bindingResult.allErrors
    val errorCount: Int
    val errorInformation: ArrayList<ErrorFunc> = ArrayList()

    init {
        this.allErrors = bindingResult.allErrors
        this.errorCount = bindingResult.errorCount
        bindingResult.fieldErrors.forEach { it ->
            this.errorInformation.add(
                ErrorFunc(
                    it.objectName,
                    it.defaultMessage!!,
                    it.rejectedValue?.toString() ?: "null",
                    it.code!!,
                ),
            )
        }
    }
}
