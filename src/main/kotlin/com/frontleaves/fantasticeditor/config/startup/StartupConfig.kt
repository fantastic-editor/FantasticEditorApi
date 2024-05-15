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

package com.frontleaves.fantasticeditor.config.startup

import com.frontleaves.fantasticeditor.annotations.KSlf4j.Companion.log
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.core.env.Environment
import org.springframework.jdbc.core.JdbcTemplate

/**
 * # 系统启动配置
 * 用于配置系统启动时的相关配置
 *
 * @since v1.0.0
 * @property environment 环境变量
 * @property jdbcTemplate JDBC 模板
 * @constructor 创建一个系统启动配置
 * @property prepare 准备算法
 *
 * @since v1.0.0
 * @author xiao_lfeng
 */
@Configuration
class StartupConfig(
    private val environment: Environment,
    private val jdbcTemplate: JdbcTemplate,
) {
    /**
     * 准备算法
     */
    private lateinit var prepare: PrepareAlgorithm

    /**
     * ## 系统启动准备检查
     * 用于定义系统启动时准备的配置
     *
     * @return CommandLineRunner
     */
    @Bean
    @Order(1)
    fun prepareStart(): CommandLineRunner? {
        return CommandLineRunner {
            log.info("========== SYSTEM PREPARE START ==========")
            prepare = PrepareAlgorithm(jdbcTemplate)
        }
    }

    /**
     * ## 系统启动准备检查
     * 用于定义系统启动完毕后需要释放的资源；
     *
     * @return CommandLineRunner
     */
    @Bean
    @Order(100)
    fun prepareFinal(): CommandLineRunner {
        return CommandLineRunner {
            log.info("[STARTUP] 初始化检查完毕")
            log.info("========== SYSTEM PREPARE FINAL ==========")
            print(
                """
                ${'\u001b'}[35;1m
                        ______            __             __  _      ______    ___ __            
                       / ____/___ _____  / /_____ ______/ /_(_)____/ ____/___/ (_) /_____  _____
                      / /_  / __ `/ __ \/ __/ __ `/ ___/ __/ / ___/ __/ / __  / / __/ __ \/ ___/
                     / __/ / /_/ / / / / /_/ /_/ (__  ) /_/ / /__/ /___/ /_/ / / /_/ /_/ / /    
                    /_/    \__,_/_/ /_/\__/\__,_/____/\__/_/\___/_____/\__,_/_/\__/\____/_/  
                       
                """.trimIndent(),
            )
            print("     \u001b[32;1m::: FantasticEditor :::                    ")
            System.out.printf("\u001b[36;1m::: %s :::\u001b[0m\n\n", "1.0.0")
        }
    }
}
