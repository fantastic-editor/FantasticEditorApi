package com.frontleaves.fantasticeditor.config.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

/**
 * # 跨域过滤器
 * 用于处理跨域请求
 *
 * @since v1.0.0
 * @author xiao_lfeng
 */
class CorsAllowFilter : Filter {
    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain?,
    ) {
        val req = request as HttpServletRequest
        val res = response as HttpServletResponse

        // 设置允许跨域的配置
        res.apply {
            contentType = "application/json;charset=UTF-8"
            characterEncoding = "UTF-8"
            setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"))
            setHeader("Access-Control-Allow-Headers", "*")
            setHeader("Access-Control-Allow-Methods", "*")
        }

        // 放行OPTIONS请求
        if ("OPTIONS" == req.method) {
            res.status = HttpServletResponse.SC_OK
            return
        } else {
            chain!!.doFilter(request, response)
        }
    }
}
