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

/**
 * # 错误码枚举类
 * 用于定义系统中的错误码
 *
 * @since v1.0.0
 * @property output String
 * @property code Int
 * @property message String
 * @constructor
 */
enum class ErrorCode(val output: String, val code: Int, val message: String) {
    REQUEST_BODY_PARAMETERS_ERROR("RequestBodyParametersError", 40001, "请求体参数错误"),
    REQUEST_PARAMETERS_ERROR("RequestParametersError", 40002, "请求参数错误"),
    REQUEST_PATH_ERROR("RequestPathError", 40003, "请求路径参数错误"),
    USER_NOT_LOGIN("UserNotLogin", 40101, "用户未登录"),
    USER_NOT_EXIST("UserNotExist", 40102, "用户不存在"),
    WRONG_PASSWORD("WrongPassword", 40103, "密码错误"),
    USER_BANNED("UserBanned", 40104, "用户被封禁或未启用"),
    CSRF_TOKEN_ERROR("CsrfTokenError", 40105, "CSRF 错误"),
    TOKEN_EXPIRED("TokenExpired", 40106, "Token 过期"),
    OVER_SPEED("OverSpeed", 42901, "操作过快"),
    PERMISSION_DENIED("PermissionDenied", 40301, "权限不足"),
    REQUEST_METHOD_NOT_ALLOWED("RequestMethodNotAllowed", 40301, "请求方法不允许"),
    VERIFY_CODE_ERROR("VerifyCodeError", 40302, "验证码错误"),
    MAIL_ERROR("MailError", 40303, "邮件发送失败"),
    OPERATION_FAILED("OperationFailed", 40304, "操作失败"),
    USER_EXIST("UserExist", 40305, "用户已存在"),
    LOGIN_ACCESS("LoginAccess", 40306, "用户已登录"),
    CHECK_FAILURE("CheckFailure", 40307, "检查失败"),
    PAGE_NOT_FOUND("PageNotFound", 40401, "页面未找到"),
    SERVER_INTERNAL_ERROR("ServerInternalError", 50001, "服务器内部错误"),
}
