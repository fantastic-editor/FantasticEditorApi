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

package com.frontleaves.fantasticeditor.services;

import com.frontleaves.fantasticeditor.dao.RoleDAO;
import com.frontleaves.fantasticeditor.exceptions.BusinessException;
import com.frontleaves.fantasticeditor.models.entity.sql.SqlRoleDO;
import com.frontleaves.fantasticeditor.models.vo.api.role.RoleCustomEditVO;
import com.frontleaves.fantasticeditor.services.interfaces.RoleService;
import com.frontleaves.fantasticeditor.utility.ErrorCode;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * 角色服务实现
 * <hr/>
 * 角色服务实现，用于实现角色相关的服务；
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleDAO roleDAO;
    private final Gson gson;

    @Override
    public boolean editCustomRole(
            @NotNull final RoleCustomEditVO roleCustomEditVO,
            @NotNull final String roleId
    ) {
        SqlRoleDO getRole = roleDAO.getRoleByRUUID(roleId);
        ArrayList<String> dontEditRole = new ArrayList<>();
        dontEditRole.add("console");
        dontEditRole.add("admin");
        dontEditRole.add("user");
        if (getRole != null) {
            if (dontEditRole.contains(getRole.getName())) {
                throw new BusinessException("不可修改内置角色", ErrorCode.OPERATION_FAILED);
            }
            // 检查新的角色名称是否已经存在
            SqlRoleDO checkRole = roleDAO.getRoleByRoleName(roleCustomEditVO.getName());
            if (checkRole != null) {
                throw new BusinessException("角色名称已存在", ErrorCode.OPERATION_FAILED);
            }
            // 更新角色信息
            getRole
                    .setName(roleCustomEditVO.getName())
                    .setDisplayName(roleCustomEditVO.getDisplayName())
                    .setDescription(roleCustomEditVO.getDescription())
                    .setUpdatedAt(new Timestamp(System.currentTimeMillis()))
                    .setPermissions(gson.toJson(roleCustomEditVO.getPermissions()));
            return roleDAO.updateById(getRole);
        } else {
            throw new BusinessException("角色不存在", ErrorCode.OPERATION_FAILED);
        }
    }
}
