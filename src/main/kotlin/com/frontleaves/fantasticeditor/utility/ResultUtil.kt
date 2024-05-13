package com.frontleaves.fantasticeditor.utility

import com.frontleaves.fantasticeditor.annotations.Slf4j.Companion.log
import org.springframework.http.ResponseEntity

object ResultUtil {
    /**
     * 成功输出
     * <hr></hr>
     * 输出一个成功的结果, 用于输出一个成功的结果, 用于输出一个成功的结果
     * 但是该输出结果不包含数据
     *
     * @param message 输出的 message 信息
     * @return 输出一个成功的结果
     */
    fun success(message: String): ResponseEntity<BaseResponse<Void>> {
        log.info("[RESULT] [{}]{} | {}", 200, "Success", "成功")
        return ResponseEntity
            .ok(BaseResponse("Success", 200, message, null, null))
    }

    /**
     * 成功输出
     * <hr></hr>
     * 输出一个成功的结果, 用于输出一个成功的结果, 用于输出一个成功的结果
     * 该输出结果包含数据
     *
     * @param message 输出的 message 信息
     * @param data    输出的数据
     * @return 输出一个成功的结果
     */
    fun <T> success(
        message: String,
        data: T,
    ): ResponseEntity<BaseResponse<T>> {
        log.info("[RESULT] [{}]{} | {}(数据: {})", 200, "Success", "成功", data!!.javaClass.simpleName)
        return ResponseEntity
            .ok(BaseResponse("Success", 200, message, null, data))
    }

    /**
     * 失败输出
     * <hr></hr>
     * 输出一个失败的结果, 用于输出一个失败的结果, 用于输出一个失败的结果, 输出的结果包含数据，需要传入错误码(ErrorCode)和错误信息；
     * 请注意，失败的输出不应该在此直接调用，应当在业务中出现错误直接进行抛出异常，然后在全局异常处理中进行处理；
     * 不应当在代码中直接处理，否则会导致代码的可读性和可维护性降低；以及无法处理数据库事务的问题；
     *
     * @param errorCode    错误码 - 用于定义系统中的错误码信息，用于统一管理系统中的错误码信息
     * @param errorMessage 错误信息 - 用于定义系统中的错误信息，用于统一管理系统中的错误信息
     * @param data         输出的数据
     * @return 输出一个失败的结果
     */
    fun <T> error(
        errorCode: ErrorCode,
        errorMessage: String,
        data: T?,
    ): ResponseEntity<BaseResponse<T>> {
        if (data != null) {
            log.info(
                "[RESULT] [{}]{} | {}:{}(数据: {})",
                errorCode.code,
                errorCode.output,
                errorCode.message,
                errorMessage,
                data.javaClass.simpleName,
            )
        } else {
            log.info("[RESULT] [{}]{} | {}:{}", errorCode.code, errorCode.output, errorCode.message, errorMessage)
        }
        return ResponseEntity
            .status(errorCode.code / 100)
            .body(BaseResponse(errorCode.output, errorCode.code, errorCode.message, errorMessage, data))
    }
}
