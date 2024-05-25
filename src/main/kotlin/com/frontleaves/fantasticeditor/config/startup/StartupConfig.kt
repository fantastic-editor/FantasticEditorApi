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
import com.frontleaves.fantasticeditor.constant.BaseDataConstant
import com.frontleaves.fantasticeditor.constant.BceDataConstant
import com.frontleaves.fantasticeditor.utility.Util
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
 * @property env 环境变量
 * @property jdbcTemplate JDBC 模板
 * @constructor 创建一个系统启动配置
 * @property prepare 准备算法
 *
 * @since v1.0.0
 * @author xiao_lfeng
 */
@Configuration
class StartupConfig(
    private val env: Environment,
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
    fun prepareStart(): CommandLineRunner {
        return CommandLineRunner {
            log.info("========== SYSTEM PREPARE START ==========")
            prepare = PrepareAlgorithm(jdbcTemplate)
        }
    }

    /**
     * ## 系统启动准备检查
     * 用于定义系统启动时进行数据表完整性的检查；该部分内容会检查已链接的数据库中是否包含所需要的数据内容，若包含则不会进行任何操作，
     * 若不包含则会自动创建数据表；注意数据表若有外键约束请注意先后顺序
     *
     * @return CommandLineRunner
     */
    @Bean
    @Order(2)
    fun prepareTable(): CommandLineRunner {
        return CommandLineRunner {
            log.info("[STARTUP] 正在进行数据表完整性检查...")
            prepare.table("info")
            prepare.table("permission")
            prepare.table("role")
            prepare.table("vip")
            prepare.table("user")
        }
    }

    /**
     * ## 系统启动准备检查
     * 用于定义 fy_info 数据表内数据内容的定义，该表是一张固定表，用于存储系统的基本信息；系统将会检查表内是否存在指定数据，
     * 若不存在则会自动创建数据
     *
     * @return CommandLineRunner
     */
    @Bean
    @Order(3)
    fun prepareInfoData(): CommandLineRunner {
        return CommandLineRunner {
            log.info("[STARTUP] 正在进行 fy_info 表完整性检查...")
            prepare.infoDataPrepare("title", "妙笔智编")
            prepare.infoDataPrepare("subTitle", "一个结合大小AI模型技术，实现智能润色、多媒体信息提取和智能格式排版等功能，以优化学习和工作体验的智能平台")
        }
    }

    /**
     * ## 系统启动准备检查
     * 用于定义 fy_permission 数据表内数据内容的定义，该表是一张固定表，用于存储系统的权限信息；系统将会检查表内是否存在指定数据，
     * 若不存在则会自动创建数据
     *
     * @return CommandLineRunner
     */
    @Bean
    @Order(4)
    fun preparePermissionData(): CommandLineRunner {
        return CommandLineRunner {
            log.info("[STARTUP] 正在进行 fy_permission 表完整性检查...")
            prepare.permission("user:userCurrent", "用户当前信息")

            prepare.addRole("console", "超级管理员", "系统最高权限")
            prepare.addRole("admin", "管理员", "系统管理员")
            prepare.addRole("user", "用户", "普通用户")
        }
    }

    @Bean
    @Order(10)
    fun prepareConsoleUser(): CommandLineRunner {
        return CommandLineRunner {
            log.info("[STARTUP] 正在进行系统管理员账户检查...")
            val superAdminUUID = Util.makeUUIDByString("SuperConsoleUserForFantasticEditor")
            try {
                jdbcTemplate.query(
                    "SELECT uuid FROM fy_user WHERE uuid = ?",
                    { rs, _ -> rs.getString("uuid") },
                    superAdminUUID.toString().replace("-", ""),
                ).first()
            } catch (e: Exception) {
                val getRole = jdbcTemplate.query(
                    "SELECT ruuid FROM fy_role WHERE name = ?",
                    { rs, _ -> rs.getString("ruuid") },
                    "console",
                ).first()
                jdbcTemplate.update(
                    """INSERT INTO fy_user
                        | (uuid, username, email, phone, password, otp_auth, basic_information, role)
                        | VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                    """.trimMargin(),
                    superAdminUUID.toString().replace("-", ""),
                    "console",
                    "console@fe.com",
                    "18888888888",
                    Util.encryptPassword("admin"),
                    Util.makeNoDashUUID(),
                    "{}",
                    getRole,
                )
            }
        }
    }

    @Bean
    @Order(90)
    fun prepareEnd(): CommandLineRunner {
        return CommandLineRunner {
            log.info("[STARTUP] 全局常量配置器")
            BceDataConstant.bceSmsTemplateID = env.getProperty("baidu.sms.template-sms-id")
            BceDataConstant.bceSmsSignatureID = env.getProperty("baidu.sms.signature-id")
            BceDataConstant.bceBusinessTemplateID = env.getProperty("baidu.sms.template-business-id")

            BceDataConstant.bosEndpoint = env.getProperty("baidu.bos.endpoint")!!
            BceDataConstant.bosBucketName = env.getProperty("baidu.bos.bucket-name")!!

            BaseDataConstant.mailHost = env.getProperty("spring.mail.host")!!
            BaseDataConstant.mailUsername = env.getProperty("spring.mail.username")!!
            BaseDataConstant.mailPassword = env.getProperty("spring.mail.password")!!
            BaseDataConstant.mailEncoding = env.getProperty("spring.mail.default-encoding")!!
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
