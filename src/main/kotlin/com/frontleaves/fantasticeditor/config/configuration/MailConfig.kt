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

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

/**
 * # 邮件配置
 * 用于配置邮件发送的配置
 *
 * @constructor 创建一个邮件配置
 * @property env 环境变量
 *
 * @since v1.0.0-SNAPSHOT
 * @author xiao_lfeng
 */
@Configuration
class MailConfig(private val env: Environment) {
    /**
     * ## Java 邮件发送器
     * 用于定义 Java 邮件发送器
     *
     * @return JavaMailSender
     */
    @Bean
    fun javaMailSender(): JavaMailSender {
        return JavaMailSenderImpl()
            .apply {
                defaultEncoding = "UTF-8"
                host = env.getProperty("spring.mail.host")
                port = 25
                username = env.getProperty("spring.mail.username")
                password = env.getProperty("spring.mail.password")
                javaMailProperties.also {
                    it["mail.smtp.auth"] = "true"
                    it["mail.smtp.starttls.enable"] = "true"
                    it["mail.debug"] = "false"
                    it["mail.transport.protocol"] = "smtp"
                }
            }
    }
}
