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

package com.frontleaves.fantasticeditor.config.configuration

import com.frontleaves.fantasticeditor.annotations.KSlf4j.Companion.log
import com.frontleaves.fantasticeditor.config.custom.CustomCsrfRepository
import com.frontleaves.fantasticeditor.config.filter.CorsAllowFilter
import com.frontleaves.fantasticeditor.config.filter.CsrfAllowFilter
import com.frontleaves.fantasticeditor.config.filter.RequestHeaderFilter
import com.frontleaves.fantasticeditor.utility.redis.RedisUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.security.web.csrf.CsrfTokenRepository

/**
 * # SpringSecurity 安全配置
 * 用于配置 SpringSecurity 安全相关的配置
 *
 * @since v1.0.0
 * @author xiao_lfeng
 */
@Configuration
class SecurityConfig(private val redisUtil: RedisUtil) {
    /**
     * 配置安全过滤器链
     *
     * @param security HttpSecurity
     * @return SecurityFilterChain
     */
    @Bean
    @Throws(Exception::class)
    fun filterChain(security: HttpSecurity): SecurityFilterChain {
        log.info("[CONFIG] 安全配置初始化...")
        return security
            .csrf { it.disable() }
            .cors { it.disable() }
            .formLogin { it.disable() }
            .addFilterAfter(CsrfAllowFilter(csrfTokenRepository()), CsrfFilter::class.java)
            .addFilterBefore(RequestHeaderFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(CorsAllowFilter(), RequestHeaderFilter::class.java)
            .authorizeHttpRequests {
                it.requestMatchers("/**")
                    .permitAll().anyRequest().permitAll()
            }
            .build()
    }

    @Bean
    fun csrfTokenRepository(): CsrfTokenRepository {
        return CustomCsrfRepository(redisUtil)
    }
}
