package com.frontleaves.fantasticeditor.config.filter

import com.frontleaves.fantasticeditor.exceptions.RequestHeaderNotMatchException
import com.frontleaves.fantasticeditor.utility.ErrorCode
import com.frontleaves.fantasticeditor.utility.ResultUtil
import com.google.gson.Gson
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

/**
 * # 请求头过滤器
 * 用于过滤请求头信息, 以保证请求的安全性, 防止恶意请求;
 *
 * @since v1.0.0
 * @author xiao_lfeng
 */
class RequestHeaderFilter : Filter {
    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain?,
    ) {
        val req = request as HttpServletRequest
        val res = response as HttpServletResponse
        try {
            // 检查请求头是否为 application/json
            val checkHasSwaggerApi = (
                req.requestURI.matches("/v3/api\\S+$".toRegex()) ||
                    req.requestURI.matches("/swagger\\S+$".toRegex())
            )
            if ("application/json" != req.contentType && !checkHasSwaggerApi) {
                throw RequestHeaderNotMatchException("请求类型需要为 application/json")
            }
            // 检查请求头是否包含正确的 User-Agent
            if (req.getHeader("User-Agent") == null || req.getHeader("User-Agent").isEmpty()) {
                throw RequestHeaderNotMatchException("请求头中缺少 User-Agent")
            }
            chain!!.doFilter(request, response)
        } catch (e: RequestHeaderNotMatchException) {
            res.status = HttpServletResponse.SC_METHOD_NOT_ALLOWED
            res.contentType = "application/json;charset=UTF-8"
            res.writer
                .write(
                    Gson().toJson(
                        ResultUtil.error(
                            ErrorCode.REQUEST_METHOD_NOT_ALLOWED,
                            e.message,
                            null,
                        ).body,
                    ),
                )
        }
    }
}
