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

package com.frontleaves.fantasticeditor.models.entity

import java.sql.Timestamp

/**
 * # 角色实体类
 * 用于定义角色实体类；
 *
 * @since v1.0.0
 * @property ruuid 角色唯一标识
 * @property name 角色名称
 * @property displayName 角色显示名称
 * @property permissions 角色权限
 * @property updatedAt 更新时间
 * @constructor 创建一个角色实体类
 * @author xiao_lfeng
 */
data class FyRoleDO(
    val ruuid: String? = null,
    val name: String? = null,
    val displayName: String? = null,
    val permissions: String? = null,
    val updatedAt: Timestamp? = null,
)
