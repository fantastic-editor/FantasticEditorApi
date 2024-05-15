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

package com.frontleaves.fantasticeditor.config.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

/**
 * # 跨域过滤器
 * 用于处理跨域请求
 *
 * @since v1.0.0
 * @author xiao_lfeng
 */
class CorsAllowFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        // 设置允许跨域的配置
        response.apply {
            contentType = "application/json;charset=UTF-8"
            characterEncoding = "UTF-8"
            setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"))
            setHeader("Access-Control-Allow-Headers", "*")
            setHeader("Access-Control-Allow-Methods", "*")
        }

        // 放行OPTIONS请求
        if ("OPTIONS" == request.method) {
            response.status = HttpServletResponse.SC_OK
            return
        } else {
            filterChain.doFilter(request, response)
        }
    }
}
