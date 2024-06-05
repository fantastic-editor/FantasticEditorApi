/*
 * ******************************************************************************* Copyright (C) 2024-NOW(至今) 妙笔智编
 * Author: 锋楪技术团队
 *
 * 本文件包含 妙笔智编「FantasticEditor」 的源代码，该项目的所有源代码均遵循MIT开源许可证协议。 本代码仅允许在十三届软件杯比赛授权比赛方可直接使用
 * ******************************************************************************* 免责声明：
 * 使用本软件的风险由用户自担。作者或版权持有人在法律允许的最大范围内， 对因使用本软件内容而导致的任何直接或间接的损失不承担任何责任。
 * *******************************************************************************
 */

package com.frontleaves.fantasticeditor.models.vo.api.role;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户角色信息实体类
 * <p>
 * 用户角色信息VO类
 *
 * @author zrx
 * @date 2024/6/4 19:03
 * @since v1.0.0
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class RoleInfoVO {
    /**
     * 角色UUID
     */
    public String ruuid;
    /**
     * 角色名称
     */
    public String name;
    /**
     * 角色显示名称
     */
    public String displayName;
    /**
     * 角色描述
     */
    public String description;
    /**
     * 角色权限
     */
    public String[] permissions;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Timestamp updatedAt;
}
