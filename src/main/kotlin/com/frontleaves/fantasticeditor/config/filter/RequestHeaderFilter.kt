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
                    req.requestURI.matches("/swagger\\S+$".toRegex()) ||
                    req.requestURI.matches("/".toRegex()) ||
                    req.requestURI.matches("/favicon.ico".toRegex())
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
