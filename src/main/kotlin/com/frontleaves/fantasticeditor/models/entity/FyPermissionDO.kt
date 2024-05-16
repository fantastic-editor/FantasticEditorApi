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

/**
 * # 权限实体类
 * 用于定义权限实体类；
 *
 * @since v1.0.0
 * @property pid 权限唯一标识
 * @property permission 权限
 * @property description 权限描述
 * @property parent 父权限
 * @constructor 创建一个权限实体类
 * @author xiao_lfeng
 */
data class FyPermissionDO(
    val pid: Long? = null,
    val permission: String? = null,
    val description: String? = null,
    val parent: Long? = null,
)
