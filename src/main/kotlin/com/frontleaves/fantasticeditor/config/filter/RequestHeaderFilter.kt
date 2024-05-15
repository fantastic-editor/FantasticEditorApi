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

import com.frontleaves.fantasticeditor.exceptions.RequestHeaderNotMatchException
import com.frontleaves.fantasticeditor.utility.ErrorCode
import com.frontleaves.fantasticeditor.utility.ResultUtil
import com.google.gson.Gson
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter

/**
 * # 请求头过滤器
 * 用于过滤请求头信息, 以保证请求的安全性, 防止恶意请求;
 *
 * @since v1.0.0
 * @author xiao_lfeng
 */
class RequestHeaderFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            // 检查请求头是否为 application/json
            val checkHasSwaggerApi = (
                request.requestURI.matches("/v3/api\\S+$".toRegex()) ||
                    request.requestURI.matches("/swagger\\S+$".toRegex()) ||
                    request.requestURI.matches("/".toRegex()) ||
                    request.requestURI.matches("/favicon.ico".toRegex())
                )
            if (request.contentType == null) {
                throw RequestHeaderNotMatchException("content-type 不能为空")
            }
            if (!request.contentType.contains("application/json") && !checkHasSwaggerApi) {
                throw RequestHeaderNotMatchException("请求类型需要为 application/json")
            }
            // 检查请求头是否包含正确的 User-Agent
            if (request.getHeader("User-Agent") == null || request.getHeader("User-Agent").isEmpty()) {
                throw RequestHeaderNotMatchException("请求头中缺少 User-Agent")
            }
            filterChain.doFilter(request, response)
        } catch (e: RequestHeaderNotMatchException) {
            response.also {
                it.status = HttpServletResponse.SC_METHOD_NOT_ALLOWED
                it.contentType = "application/json;charset=UTF-8"
                it.writer
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
}
