package com.frontleaves.fantasticeditor.config.startup

import com.frontleaves.fantasticeditor.annotations.Slf4j.Companion.log
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
