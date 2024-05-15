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

import com.frontleaves.fantasticeditor.annotations.KSlf4j.Companion.log
import com.frontleaves.fantasticeditor.utility.ErrorCode
import com.frontleaves.fantasticeditor.utility.ResultUtil
import com.google.gson.Gson
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.csrf.CsrfTokenRepository
import org.springframework.web.filter.OncePerRequestFilter

/**
 * # CSRF 允许配置
 * 用于配置 CSRF 允许
 *
 * @since v1.0.0
 * @constructor 创建一个 CSRF 允许配置
 * @property csrfRepository 自定义 CSRF 仓库
 */
class CsrfAllowFilter(
    private val csrfRepository: CsrfTokenRepository,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        log.debug("[FILTER] 检查 CSRF Token...")
        if ("POST" == request.method || "post" == request.method) {
            val csrfToken = csrfRepository.loadToken(request)
            val getToken = request.getHeader(csrfToken.headerName)
            if (getToken != null && getToken == csrfToken.token) {
                filterChain.doFilter(request, response)
            } else {
                response.also {
                    it.status = HttpServletResponse.SC_UNAUTHORIZED
                    it.contentType = "application/json;charset=UTF-8"
                    it.writer
                        .write(
                            Gson().toJson(
                                ResultUtil.error(
                                    ErrorCode.CSRF_TOKEN_ERROR,
                                    "Token 不存在或不正确",
                                    null,
                                ).body,
                            ),
                        )
                }
            }
        } else {
            filterChain.doFilter(request, response)
        }
    }
}
