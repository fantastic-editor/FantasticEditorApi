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

package com.frontleaves.fantasticeditor.config.handler

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import org.postgresql.util.PGobject
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

/**
 * ## JSON 类型处理器
 * 用于处理 JSON 类型的数据
 *
 * @since v1.0.0
 * @constructor 创建一个 JSON 类型处理器
 * @return String
 * @author xiao_lfeng
 */
class JsonTypeHandler : BaseTypeHandler<String>() {

    companion object {
        private val objectMapper = ObjectMapper()
    }

    @Throws(SQLException::class)
    override fun setNonNullParameter(ps: PreparedStatement, i: Int, parameter: String, jdbcType: JdbcType) {
        val pGobject = PGobject()
        pGobject.type = "json"
        try {
            pGobject.value = objectMapper.writeValueAsString(parameter)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        ps.setObject(i, pGobject)
    }

    @Throws(SQLException::class)
    override fun getNullableResult(rs: ResultSet, columnName: String): String {
        return rs.getString(columnName)
    }

    @Throws(SQLException::class)
    override fun getNullableResult(rs: ResultSet, columnIndex: Int): String {
        return rs.getString(columnIndex)
    }

    @Throws(SQLException::class)
    override fun getNullableResult(cs: CallableStatement, columnIndex: Int): String {
        return cs.getString(columnIndex)
    }
}
