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

@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.frontleaves.fantasticeditor.exceptions

import com.frontleaves.fantasticeditor.annotations.KSlf4j.Companion.log
import com.frontleaves.fantasticeditor.exceptions.library.*
import com.frontleaves.fantasticeditor.utility.BaseResponse
import com.frontleaves.fantasticeditor.utility.ErrorCode
import com.frontleaves.fantasticeditor.utility.ResultUtil
import org.springframework.http.ResponseEntity
import org.springframework.mail.MailSendException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.resource.NoResourceFoundException
import java.util.*

/**
 *  # 业务异常处理
 *  用于处理业务异常, 该类中的方法用于处理自定义的业务异常，当业务异常发生时，将会自动捕获并处理，不会影响系统的正常运行
 *
 *  @since v1.0.0
 *  @author xiao_lfeng
 */
@RestControllerAdvice
class PublicExceptionHandler {
    /**
     * ## 异常处理
     * 用于处理业务异常, 当业务异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 异常信息 Exception
     * @return 返回异常信息
     */
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<BaseResponse<Exception>> {
        log.error("[EXCEPTION] 未定义输出异常 | {}", e.message, e)
        return ResultUtil.error(ErrorCode.SERVER_INTERNAL_ERROR, "未定义输出异常", e)
    }

    /**
     * ## 页面未找到异常处理
     * 用于处理页面未找到异常, 当页面未找到异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 页面未找到异常 PageNotFoundedException
     * @return 返回异常信息
     */
    @ExceptionHandler(PageNotFoundedException::class)
    fun handlePageNotFoundException(e: PageNotFoundedException): ResponseEntity<BaseResponse<PageNotFoundedException>> {
        log.warn("[EXCEPTION] 页面未找到异常 | {}<{}>", e.message, e.route)
        return ResultUtil.error(ErrorCode.PAGE_NOT_FOUND, "页面未找到", e)
    }

    /**
     * ## 业务异常处理
     * 用于处理业务异常, 当业务异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 业务异常 BusinessException
     * @return 返回业务异常信息
     */
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<BaseResponse<Any>> {
        if (e.errorOutput) {
            log.warn("[EXCEPTION] <{}>{} | {}", e.errorCode.code, e.errorCode.message, e.errorMessage, e)
            return ResultUtil.error(e.errorCode, e.errorMessage, e)
        } else {
            log.warn("[EXCEPTION] <{}>{} | {}", e.errorCode.code, e.errorCode.message, e.errorMessage)
            return ResultUtil.error(e.errorCode, e.errorMessage, e.data)
        }
    }

    /**
     * ## 空指针异常处理
     * 用于处理空指针异常, 当空指针异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 空指针异常 NullPointerException
     * @return 返回空指针异常信息
     */
    @ExceptionHandler(NullPointerException::class)
    fun handleNullPointerException(e: NullPointerException): ResponseEntity<BaseResponse<NullPointerException>> {
        log.error("[EXCEPTION] 空指针异常 | {}", e.message, e)
        return ResultUtil.error(ErrorCode.SERVER_INTERNAL_ERROR, "空指针异常", e)
    }

    /**
     * ## 请求方法不支持异常处理
     * 用于处理请求方法不支持异常, 当请求方法不支持异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 请求方法不支持异常 HttpRequestMethodNotSupportedException
     * @return 返回请求方法不支持异常信息
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(
        e: HttpRequestMethodNotSupportedException,
    ): ResponseEntity<BaseResponse<HashMap<String, Any>>> {
        log.warn("[EXCEPTION] 请求方法不支持 | 获取的方法 [{}] ,需要的方法 {}", e.method, e.supportedHttpMethods)
        val data = HashMap<String, Any>()
        data["method"] = ArrayList<String>().apply { add(Objects.requireNonNull(e.method).toString()) }
        data["supported"] = Objects.requireNonNull(e.supportedHttpMethods).toString()
        return ResultUtil.error(ErrorCode.REQUEST_METHOD_NOT_ALLOWED, "请求方法不支持", data)
    }

    /**
     * ## 请求头缺失异常处理
     * 用于处理请求头缺失异常, 当请求头缺失异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 请求头缺失异常 MissingRequestHeaderException
     * @return 返回请求头缺失异常信息
     */
    @ExceptionHandler(MissingRequestHeaderException::class)
    fun handleMissingRequestHeaderException(e: MissingRequestHeaderException): ResponseEntity<BaseResponse<Void>> {
        log.warn("[EXCEPTION] 请求头缺失 | 缺失的请求头 [{}]", e.headerName)
        return when (e.headerName) {
            "Authorization", "X-User-UUID" ->
                ResultUtil.error(
                    ErrorCode.USER_NOT_LOGIN,
                    "用户未登录",
                    null,
                )
            else ->
                ResultUtil.error(
                    ErrorCode.REQUEST_METHOD_NOT_ALLOWED,
                    "请求头 " + e.headerName + " 缺失",
                    null,
                )
        }
    }

    /**
     * ## 请求体参数异常处理
     * 用于处理请求体参数异常, 当请求体参数异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 请求体参数异常 RequestBodyParametersException
     * @return 返回请求体参数异常信息
     */
    /*@ExceptionHandler(RequestBodyParametersException::class)
    fun handleRequestBodyParametersException(
        e: RequestBodyParametersException,
    ): ResponseEntity<BaseResponse<List<RequestBodyParametersException.ErrorFunc>>> {
        log.warn("[EXCEPTION] 请求体参数错误 | {} (...{}个错误)", e.errorInformation[0].message, e.errorCount - 1)
        return ResultUtil.error(
            ErrorCode.REQUEST_BODY_PARAMETERS_ERROR,
            e.errorInformation[0].message,
            e.errorInformation,
        )
    }*/

    /**
     * ## 请求路径参数异常处理
     * 用于处理请求路径参数异常, 当请求路径参数异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 请求路径参数异常 RequestPathParametersException
     * @return 返回请求路径参数异常信息
     */
    @ExceptionHandler(RequestHeaderNotMatchException::class)
    fun handleRequestHeaderNotMatchException(
        e: RequestHeaderNotMatchException,
    ): ResponseEntity<BaseResponse<RequestHeaderNotMatchException>> {
        log.warn("[EXCEPTION] 请求头不匹配异常 | {}", e.message)
        return ResultUtil.error(ErrorCode.REQUEST_METHOD_NOT_ALLOWED, e.message, null)
    }

    /**
     * ## 请求参数异常处理
     * 用于处理请求参数异常, 当请求参数异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 请求参数异常 RequestParametersException
     * @return 返回请求参数异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<BaseResponse<List<String>>> {
        log.warn("[EXCEPTION] 参数校验错误 | 错误 {} 个 ", e.bindingResult.errorCount)
        e.fieldErrors.forEach {
            log.debug("\t\t<{}>[{}]: {}", it.field, it.rejectedValue, it.defaultMessage)
        }
        return ResultUtil.error(
            ErrorCode.REQUEST_BODY_PARAMETERS_ERROR,
            e.allErrors.map { it.defaultMessage!! }.toList()[0],
            e.allErrors.map { it.defaultMessage!! }.toList(),
        )
    }

    /**
     * ## 邮件模板不存在异常处理
     * 用于处理邮件模板不存在异常, 当邮件模板不存在异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 邮件模板不存在异常 MailTemplateNotFoundException
     * @return 返回邮件模板不存在异常信息
     */
    @ExceptionHandler(MailTemplateNotFoundException::class)
    fun handleMailTemplateNotFoundException(e: MailTemplateNotFoundException): ResponseEntity<BaseResponse<MailTemplateNotFoundException>> {
        log.warn("[EXCEPTION] 邮件模板不存在 | {}", e.message)
        return ResultUtil.error(ErrorCode.MAIL_ERROR, "邮件模板 ${e.message} 不存在", null)
    }

    /**
     * ## 邮件发送异常处理
     * 用于处理邮件发送异常, 当邮件发送异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 邮件发送异常 MailSendException
     * @return 返回邮件发送异常信息
     */
    @ExceptionHandler(MailSendException::class)
    fun handleMailSendException(e: MailSendException): ResponseEntity<BaseResponse<MailSendException>> {
        log.error("[EXCEPTION] 邮件发送异常 | {}", e.message, e)
        return ResultUtil.error(ErrorCode.MAIL_ERROR, "邮件发送异常", e)
    }

    /**
     * ## 用户认证异常处理
     * 用于处理用户认证异常, 当用户认证异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 用户认证异常 UserAuthenticationException
     * @return 返回用户认证异常信息
     */
    @ExceptionHandler(UserAuthenticationException::class)
    fun handleUserAuthenticationException(
        e: UserAuthenticationException,
    ): ResponseEntity<BaseResponse<UserAuthenticationException.UserInfo>> {
        log.error("[EXCEPTION] 用户认证异常 | {}", e.message, e)
        return ResultUtil.error(e.errorType.errorCode, e.errorType.message, e.userInfo)
    }

    /**
     * ## 服务器内部错误异常处理
     * 用于处理服务器内部错误异常, 当服务器内部错误异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 服务器内部错误 ServerInternalErrorException
     * @return 返回服务器内部错误异常信息
     */
    @ExceptionHandler(ServerInternalErrorException::class)
    fun handleServerInternalErrorException(e: ServerInternalErrorException): ResponseEntity<BaseResponse<ServerInternalErrorException>> {
        log.error("[EXCEPTION] 服务器内部错误 | {}", e.message, e)
        return ResultUtil.error(ErrorCode.SERVER_INTERNAL_ERROR, e.message, e)
    }

    /**
     * ## 检查失败异常处理
     * 用于处理检查失败异常, 当检查失败异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 检查失败异常 CheckFailureException
     * @return 返回检查失败异常信息
     */
    @ExceptionHandler(CheckFailureException::class)
    fun handleCheckFailureException(e: CheckFailureException): ResponseEntity<BaseResponse<CheckFailureException>> {
        log.error("[EXCEPTION] 检查失败异常 | {}", e.message)
        return ResultUtil.error(ErrorCode.CHECK_FAILURE, e.message, null)
    }

    /**
     * ## 资源未找到异常处理
     * 用于处理资源未找到异常, 当资源未找到异常发生时，将会自动捕获并处理，不会影响系统的正常运行
     *
     * @param e 资源未找到异常 NoResourceFoundException
     * @return 返回资源未找到异常信息
     */
    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResourceFoundException(e: NoResourceFoundException): ResponseEntity<BaseResponse<NoResourceFoundException>> {
        log.error("[EXCEPTION] 资源未找到异常 | {}", e.message, e)
        return ResultUtil.error(ErrorCode.PAGE_NOT_FOUND, "资源未找到", e)
    }
}
