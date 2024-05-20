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

import com.frontleaves.fantasticeditor.utility.ErrorCode
import jakarta.servlet.http.HttpServletRequest

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
        TOKEN_EXPIRED("令牌过期或不存在", ErrorCode.TOKEN_EXPIRED),
        PERMISSION_DENIED("权限不足", ErrorCode.PERMISSION_DENIED),
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
