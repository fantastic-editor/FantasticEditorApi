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
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.util.FileCopyUtils
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.sql.Timestamp
import java.util.*

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
            jdbcTemplate.query(
                "SELECT table_name FROM information_schema.tables WHERE table_name = ?",
                { rs, _ -> rs.getString("table_name") },
                "fy_$tableName",
            ).first()
        } catch (e: NoSuchElementException) {
            log.debug("[STARTUP] 创建数据表 {}", tableName)
            // 读取 resources/sql 指定 SQL 文件
            val resource = ClassPathResource("/sql/fy_$tableName.sql")
            // 创建数据表
            try {
                val sql = FileCopyUtils.copyToString(InputStreamReader(resource.inputStream, StandardCharsets.UTF_8))
                sql
                    .replace(Regex("""(?s)/\*.*?\*/"""), "")
                    .split(";")
                    .forEach { statement -> jdbcTemplate.execute(statement) }
            } catch (ex: IOException) {
                log.error("[STARTUP] 创建数据表失败 | {}", ex.message, ex)
            }
        }
    }

    /**
     * ## 准备数据
     * 用于准备数据，准备 fy_info 数据表的基本数据
     *
     * @param key 键
     * @param value 值
     */
    fun infoDataPrepare(key: String, value: String) {
        try {
            jdbcTemplate.query(
                "SELECT key FROM fy_info WHERE key = ?",
                { rs, _ -> rs.getString("key") },
                key,
            ).first()
        } catch (e: NoSuchElementException) {
            log.debug("[STARTUP] 准备 fy_info 表数据 {}", key)
            jdbcTemplate.update(
                "INSERT INTO fy_info (id, key, value, updated_at) VALUES (?, ?, ?, ?)",
                UUID.randomUUID().toString(),
                key,
                value,
                Timestamp(System.currentTimeMillis()),
            )
        }
    }

    /**
     * ## 准备权限数据
     * 用于准备权限数据
     *
     * @param permission 权限
     * @param description 描述
     * @param parentPermission 父权限
     */
    fun permission(permission: String, description: String, parentPermission: String?) {
        try {
            jdbcTemplate.query(
                "SELECT pid FROM fy_permission WHERE permission = ?",
                { rs, _ -> rs.getLong("pid") },
                permission,
            ).first()
        } catch (e: NoSuchElementException) {
            var parentPid: Long? = null
            log.debug("[STARTUP] 准备 fy_permission 表数据 {}", permission)
            takeIf { !parentPermission.isNullOrBlank() }?.let {
                // 查询父权限是否存在
                try {
                    parentPid = jdbcTemplate.query(
                        "SELECT pid FROM fy_permission WHERE permission = ?",
                        { rs, _ -> rs.getLong("pid") },
                        parentPermission,
                    ).first()
                } catch (e: NoSuchElementException) {
                    log.error("[STARTUP] 父权限不存在 | {}", parentPermission)
                    throw e
                }
            }
            jdbcTemplate.update(
                "INSERT INTO fy_permission (permission, description, parent) VALUES (?, ?, ?)",
                permission,
                description,
                parentPid,
            )
        }
    }

    /**
     * ## 添加角色
     * 用于添加角色, 用于系统启动时添加角色
     *
     * @param roleName 角色名
     * @param displayName 显示名
     */
    fun addRole(roleName: String, displayName: String, description: String) {
        try {
            jdbcTemplate.query(
                "SELECT ruuid FROM fy_role WHERE name = ?",
                { rs, _ -> rs.getString("ruuid") },
                roleName,
            ).first()
        } catch (e: NoSuchElementException) {
            log.debug("[STARTUP] 准备 fy_role 表数据 {}", roleName)
            jdbcTemplate.update(
                "INSERT INTO fy_role (ruuid, name, display_name, description, permissions) VALUES (?, ?, ?, ?, ?::jsonb)",
                UUID.randomUUID().toString().replace("-", ""),
                roleName,
                displayName,
                description,
                "[]",
            )
        }
    }
}
