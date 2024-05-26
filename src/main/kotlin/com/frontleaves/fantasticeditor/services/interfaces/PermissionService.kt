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

package com.frontleaves.fantasticeditor.services.interfaces

import com.frontleaves.fantasticeditor.models.entity.sql.SqlPermissionDO

/**
 * ## 用户权限接口
 * 用于定义用户权限接口
 *
 * @since v1.0.0
 * @constructor 创建一个用户权限接口
 * @author DC_DC
 */

interface PermissionService {

    /**
     * ## 获取权限列表
     * 获取权限列表, 返回权限列表, 可根据搜索条件进行搜索, 并分页返回; 若搜索条件为空, 则返回所有权限
     *
     * @param search 搜索条件
     * @param page 页码
     * @param size 每页大小
     * @return 权限列表
     */
    fun getPermissionList(search: String?, page: Int?, size: Int?): List<SqlPermissionDO>
}
