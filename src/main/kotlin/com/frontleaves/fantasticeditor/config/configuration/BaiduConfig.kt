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

import com.baidubce.auth.DefaultBceCredentials
import com.baidubce.services.sms.SmsClient
import com.baidubce.services.sms.SmsClientConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class BaiduConfig(
    private val env: Environment,
) {

    @Bean
    fun smsService(): SmsClient {
        val smsConfig = SmsClientConfiguration().also { config ->
            config.credentials = DefaultBceCredentials(
                env.getProperty("baidu.access-key"),
                env.getProperty("baidu.secret-key"),
            )
            config.endpoint = "https://smsv3.bj.baidubce.com/"
        }
        return SmsClient(smsConfig)
    }
}
