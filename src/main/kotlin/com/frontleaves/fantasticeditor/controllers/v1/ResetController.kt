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

package com.frontleaves.fantasticeditor.controllers.v1

import com.frontleaves.fantasticeditor.annotations.KSlf4j.Companion.log
import com.frontleaves.fantasticeditor.context
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 重置控制器
 *
 * 用于重置系统的控制器
 *
 * @since v1.0.0
 * @constructor 创建一个重置控制器
 */
@RestController
@RequestMapping("/api/v1/reset")
class ResetController(
    private val jdbcTemplate: JdbcTemplate,
) {

    /**
     * 重置数据
     *
     * 用于重置数据库操作的控制器
     */
    @GetMapping("/database")
    fun resetDatabase() {
        val getTable = jdbcTemplate.query(
            "SELECT table_name FROM information_schema.tables WHERE table_name LIKE ?",
            { rs, _ -> rs.getString("table_name") },
            "%fy_%",
        ).toList()
        getTable.forEach { table ->
            jdbcTemplate.execute("DROP TABLE IF EXISTS $table CASCADE")
        }
        log.info("[SERVER] 数据库已重置，所有数据表已删除，请重新启动系统！")
        context.close()
    }
}
