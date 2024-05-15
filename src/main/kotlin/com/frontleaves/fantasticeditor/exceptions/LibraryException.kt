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
import jakarta.servlet.http.HttpServletRequest
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError

/**
 * # 邮件模板未找到异常
 * 用于定义邮件模板未找到异常；
 *
 * @since v1.0.0
 * @see RuntimeException
 * @property message 异常信息
 * @constructor 创建一个邮件模板未找到异常
 * @author xiao_lfeng
 */
class MailTemplateNotFoundException(override val message: String) : RuntimeException(message)

/**
 * # 页面未找到异常
 * 用于定义页面未找到异常；
 *
 * @since v1.0.0
 * @see RuntimeException
 * @property route 页面路由
 * @constructor 创建一个页面未找到异常
 * @author xiao_lfeng
 */
class PageNotFoundedException(override val message: String, val route: String) : RuntimeException(message) {
    constructor(route: String) : this("页面未找到", route)
}

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

/**
 * # 请求头未匹配异常
 * 用于定义请求头需要匹配对应参数异常、参数不存在、参数错误等；
 *
 * @since v1.0.0
 * @see RuntimeException
 * @property message 异常信息
 * @constructor 创建一个请求参数异常
 * @author xiao_lfeng
 */
class RequestHeaderNotMatchException(override val message: String) : RuntimeException(message)

/**
 * # 系统参数异常
 * 用于定义系统参数异常；
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

/**
 * # 用户认证异常
 * 用于定义用户认证异常；
 *
 * @since v1.0.0
 * @see RuntimeException
 * @property errorType 选择一个异常类型
 * @property request 请求
 * @constructor 创建一个用户认证异常
 * @author xiao_lfeng
 */
class UserAuthenticationException(
    val errorType: ErrorType,
    val request: HttpServletRequest,
) : RuntimeException(errorType.message) {
    /**
     * # 错误类型枚举类
     * 用于定义用户认证异常的错误类型
     *
     * @since v1.0.0
     * @property message 错误信息
     * @constructor 创建一个错误类型
     */
    enum class ErrorType(val message: String, val errorCode: ErrorCode) {
        USER_NOT_LOGIN("用户未登录", ErrorCode.USER_NOT_LOGIN),
        USER_NOT_EXIST("用户不存在", ErrorCode.USER_NOT_EXIST),
        WRONG_PASSWORD("密码错误", ErrorCode.WRONG_PASSWORD),
        USER_BANNED("用户被封禁", ErrorCode.USER_BANNED),
    }

    /**
     * # 用户信息
     * 用于定义用户信息
     *
     * @since v1.0.0
     * @property userIp 用户IP
     * @property requestUrl 请求URL
     * @property requestMethod 请求方法
     * @property userAgent 用户代理
     * @property userToken 用户令牌
     * @property userUUID 用户UUID
     * @constructor 创建一个用户信息
     */
    data class UserInfo(
        var userIp: String?,
        var requestUrl: String?,
        var requestMethod: String?,
        var userAgent: String?,
        var userToken: String?,
        var userUUID: String?,
    )

    val userInfo =
        UserInfo(
            request.remoteAddr,
            request.requestURL.toString(),
            request.method,
            request.getHeader("User-Agent"),
            request.getHeader("Authorization"),
            request.getHeader("UUID"),
        )
}

class ServerInternalErrorException(override val message: String) : RuntimeException(message)
