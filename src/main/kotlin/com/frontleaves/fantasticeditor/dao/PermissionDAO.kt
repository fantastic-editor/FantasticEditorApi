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

package com.frontleaves.fantasticeditor.dao

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.frontleaves.fantasticeditor.mappers.PermissionMapper
import com.frontleaves.fantasticeditor.models.entity.sql.SqlPermissionDO
import org.springframework.stereotype.Repository

/**
 * # 权限数据访问对象
 * 用于访问权限数据
 *
 * @since v1.0.0
 * @constructor 创建一个权限数据访问对象
 */
@Repository
class PermissionDAO : ServiceImpl<PermissionMapper, SqlPermissionDO>(), IService<SqlPermissionDO> {
    /**
     * ## 获取所有权限
     * 获取所有权限, 返回权限列表
     *
     * @return 权限列表
     */
    fun getAllPermissions(): List<SqlPermissionDO> {
        return this.list()
    }

    /**
     * ## 根据搜索条件获取权限列表
     *
     * @param search 搜索条件
     * @param page 页码
     * @param size 每页大小
     * @return 权限列表
     */
    fun getPermissionsBySearch(search: String?, page: Long, size: Long): List<SqlPermissionDO> {
        return if (search.isNullOrBlank()) {
            this.page(Page(page, size)).records
        } else {
            this.page(
                Page(page, size),
                QueryWrapper<SqlPermissionDO>()
                    .like(SqlPermissionDO::permission.name, search)
                    .or()
                    .like(SqlPermissionDO::description.name, search),
            ).records
        }
    }
}
