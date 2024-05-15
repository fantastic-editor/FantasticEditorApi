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
import org.springframework.core.io.ClassPathResource
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.util.FileCopyUtils
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * # 准备算法
 * 用于系统启动时的准备算法，用于检查数据库是否完整，如果数据库不完整则会自动创建数据库
 *
 * @since v1.0.0
 * @property jdbcTemplate JDBC 模板
 * @constructor 创建一个准备算法
 * @param jdbcTemplate JDBC 模板
 */
internal class PrepareAlgorithm(private val jdbcTemplate: JdbcTemplate) {
    /**
     * ## 检查数据库是否完整
     * 用于检查数据库是否完整，如果数据库不完整则会自动创建数据库
     *
     * @param tableName 表名
     */
    fun table(tableName: String) {
        try {
            // 检查数据库是否完整
            jdbcTemplate.queryForObject(
                "SELECT table_name FROM information_schema.tables WHERE table_name = ?",
                String::class.java,
                "fy_$tableName",
            )
        } catch (e: DataAccessException) {
            log.debug("[STARTUP] 创建数据表 {}", tableName)
            // 读取 resources/sql 指定 SQL 文件
            val resource = ClassPathResource("/sql/$tableName.sql")
            // 创建数据表
            try {
                val sql = FileCopyUtils.copyToString(InputStreamReader(resource.inputStream, StandardCharsets.UTF_8))
                val sqlStatements = sql.split(";")
                sqlStatements.forEach { statement -> jdbcTemplate.execute(statement) }
            } catch (ex: IOException) {
                log.error("[STARTUP] 创建数据表失败 | {}", ex.message, ex)
            }
        }
    }
}
