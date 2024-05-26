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
package com.frontleaves.fantasticeditor.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当前用户权限数据传输对象
 * <p>
 * 用于返回当前用户的权限信息; 有关用户的权限和权限描述;
 * 在 {@code PermissionController} 中使用;
 *
 * @since v1.0.0
 * @author DC_DC
 * Date: 2024/5/23/19:36
 */
@Data
@NoArgsConstructor
public class GetPermissionDTO {
    // 权限
    public String permission;
    // 权限描述
    public String description;
}
