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

import java.sql.Timestamp;

/**
 * 当前用户信息数据传输对象
 * <p>
 * 用于返回当前用户信息; 有关用户的基本信息, 角色组, VIP组等;
 * 在 {@code UserController} 中使用;
 *
 * @since v1.0.0
 * @author xiao_lfeng
 */
@Data
@NoArgsConstructor
public class UserCurrentDTO {
    // 用户唯一标识
    public String uuid;
    // 用户名
    public String username;
    // 邮箱
    public String email;
    // 手机号
    public String phone;
    // 头像
    public String avatar;
    // 基本信息
    public String basicInformation;
    // 角色组
    public String role;
    // VIP组
    public String vip;
    // 创建时间
    public Timestamp createdAt;
    // 更新时间
    public Timestamp updatedAt;
}
