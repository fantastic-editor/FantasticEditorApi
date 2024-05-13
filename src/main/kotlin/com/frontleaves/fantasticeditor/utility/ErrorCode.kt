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
    USER_NOT_LOGIN("UserNotLogin", 40101, "用户未登录"),
    USER_NOT_EXIST("UserNotExist", 40102, "用户不存在"),
    WRONG_PASSWORD("WrongPassword", 40103, "密码错误"),
    USER_BANNED("UserBanned", 40104, "用户被封禁或未启用"),
    REQUEST_METHOD_NOT_ALLOWED("RequestMethodNotAllowed", 40301, "请求方法不允许"),
    VERIFY_CODE_ERROR("VerifyCodeError", 40302, "验证码错误"),
    MAIL_ERROR("MailError", 40303, "邮件发送失败"),
    OPERATION_FAILED("OperationFailed", 40304, "操作失败"),
    REQUEST_BODY_PARAMETERS_ERROR("RequestBodyParametersError", 40001, "请求体参数错误"),
    REQUEST_PARAMETERS_ERROR("RequestParametersError", 40002, "请求参数错误"),
    REQUEST_PATH_ERROR("RequestPathError", 40003, "请求路径参数错误"),
    USER_EXIST("UserExist", 40004, "用户已存在"),
    LOGIN_ACCESS("LoginAccess", 40005, "用户已登录"),
    PAGE_NOT_FOUND("PageNotFounded", 40401, "页面未找到"),
    SERVER_INTERNAL_ERROR("ServerInternalError", 50001, "服务器内部错误"),
}
