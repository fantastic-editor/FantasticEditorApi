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

package com.frontleaves.fantasticeditor.models.vo.api.role;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 角色自定义编辑视图对象
 * <hr/>
 * 角色自定义编辑视图对象，用于接收角色自定义编辑的数据；用于编辑角色时需要传入的参数信息内容；
 *
 * @since v1.0.0
 * @version v1.0.0
 * @author xiao_lfeng
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class RoleCustomEditVO {
    @NotBlank(message = "角色名称不能为空")
    public String name;

    @NotBlank(message = "角色显示名称不能为空")
    public String displayName;

    @NotBlank(message = "角色描述不能为空")
    public String description;

    @NotBlank(message = "角色权限不能为空")
    public String[] permissions;
}

