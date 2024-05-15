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

package com.frontleaves.fantasticeditor.config.custom

import com.frontleaves.fantasticeditor.utility.redis.RedisUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.csrf.CsrfToken
import org.springframework.security.web.csrf.CsrfTokenRepository
import java.util.*

/**
 * # CSRF 配置
 * 用于配置 CSRF 相关的配置
 *
 * @since v1.0.0
 * @author xiao_lfeng
 */
class CustomCsrfRepository(private val redisUtil: RedisUtil) : CsrfTokenRepository {
    companion object {
        private const val CSRF_HEADER_NAME = "X-CSRF-TOKEN"
        private const val CSRF_PARAMETER_NAME = "csrf"
    }

    /**
     * 生成 CSRF Token
     *
     * @param request HttpServletRequest
     * @return CsrfToken
     */
    override fun generateToken(request: HttpServletRequest?): CsrfToken {
        val generateToken = UUID.randomUUID().toString()
        // 将内容存到 Redis 中
        redisUtil.hashSet(
            "csrf:$generateToken",
            HashMap<String, String?>().also {
                it["token"] = generateToken
                it["headerName"] = CSRF_HEADER_NAME
                it["parameterName"] = CSRF_PARAMETER_NAME
                it["userIP"] = request?.remoteAddr
                it["userAgent"] = request?.getHeader("User-Agent")
            },
            900L,
        )
        return CustomCsrfToken(CSRF_HEADER_NAME, CSRF_PARAMETER_NAME, generateToken)
    }

    /**
     * 保存 CSRF Token
     *
     * @param token CsrfToken?
     * @param request HttpServletRequest?
     * @param response HttpServletResponse?
     */
    override fun saveToken(token: CsrfToken?, request: HttpServletRequest, response: HttpServletResponse) {
        response.setHeader(CSRF_HEADER_NAME, token?.token ?: "")
    }

    /**
     * 加载 CSRF Token
     *
     * @param request HttpServletRequest?
     * @return CsrfToken
     */
    override fun loadToken(request: HttpServletRequest?): CsrfToken {
        val getData = redisUtil.hashGet("csrf:${request?.getHeader(CSRF_HEADER_NAME)}")
        return takeIf { getData != null }?.let {
            redisUtil.delete("csrf:${request?.getHeader(CSRF_HEADER_NAME)}")
            CustomCsrfToken(
                CSRF_HEADER_NAME,
                CSRF_PARAMETER_NAME,
                getData!!["token"] as String,
            )
        } ?: CustomCsrfToken(CSRF_HEADER_NAME, CSRF_PARAMETER_NAME, null)
    }
}
