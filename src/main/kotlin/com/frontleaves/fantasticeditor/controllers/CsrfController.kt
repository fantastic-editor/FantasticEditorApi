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

package com.frontleaves.fantasticeditor.controllers

import com.frontleaves.fantasticeditor.models.dto.CsrfGetDTO
import com.frontleaves.fantasticeditor.utility.BaseResponse
import com.frontleaves.fantasticeditor.utility.ResultUtil
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.security.web.csrf.CsrfTokenRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * CSRF 控制器
 *
 * 用于处理 CSRF 相关请求
 *
 * @since v1.0.0
 * @constructor 创建一个 CSRF 控制器
 * @author xiao_lfeng
 */
@RestController
@RequestMapping("/api/v1/csrf")
class CsrfController(
    private val csrfTokenRepository: CsrfTokenRepository,
) {

    /**
     * 获取 CSRF Token
     *
     * 用于获取 CSRF Token
     */
    @GetMapping("/token")
    fun getCsrfToken(request: HttpServletRequest): ResponseEntity<BaseResponse<CsrfGetDTO>> {
        val csrfGetDTO = CsrfGetDTO()
        csrfGetDTO.token = csrfTokenRepository.generateToken(request).token
        return ResultUtil.success("获取 CSRF Token 成功", csrfGetDTO)
    }
}
