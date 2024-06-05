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
import com.frontleaves.fantasticeditor.dao.UserDAO;
import com.frontleaves.fantasticeditor.exceptions.BusinessException;
import com.frontleaves.fantasticeditor.models.entity.sql.SqlRoleDO;
import com.frontleaves.fantasticeditor.models.entity.sql.SqlUserDO;
import com.frontleaves.fantasticeditor.models.vo.api.role.RoleCustomEditVO;
import com.frontleaves.fantasticeditor.models.vo.api.role.RoleInfoVO;
import com.frontleaves.fantasticeditor.services.interfaces.RoleService;
import com.frontleaves.fantasticeditor.utility.ErrorCode;
import com.frontleaves.fantasticeditor.utility.Util;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    private final UserDAO userDAO;
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

    @Override
    public boolean addRole(@NotNull final RoleCustomEditVO roleCustomEditVO) {
        // 检查新的角色名称是否已经存在
        SqlRoleDO checkRole = roleDAO.getRoleByRoleName(roleCustomEditVO.getName());
        if (checkRole != null) {
            throw new BusinessException("角色名称已存在", ErrorCode.OPERATION_FAILED);
        }
        SqlRoleDO newRole = new SqlRoleDO()
                .setRuuid(Util.INSTANCE.makeNoDashUUID())
                .setName(roleCustomEditVO.getName())
                .setDisplayName(roleCustomEditVO.getDisplayName())
                .setDescription(roleCustomEditVO.getDescription())
                .setUpdatedAt(new Timestamp(System.currentTimeMillis()))
                .setPermissions(gson.toJson(roleCustomEditVO.getPermissions()));
        return roleDAO.addRole(newRole);
    }


    @Override
    public List<RoleInfoVO> getUserRoleInfo(@NotNull final List<String> uuids,
                                            @NotNull final List<String> usernames,
                                            @NotNull final List<String> ruuids,
                                            @NotNull final List<String> roleNames) {

        List<RoleInfoVO> roleInfoVoS = new ArrayList<>();

        // 根据用户uuid获取角色信息
        for (String uuid : uuids) {
            //  根据uuid获取用户信息
            SqlUserDO sqlUserDO = userDAO.getUserByUUID(uuid);
            if (sqlUserDO == null) {
                continue;
            }

            //  根据用户的ruuid查询角色
            if (sqlUserDO.getRole() != null ) {
                //  封装数据
                RoleInfoVO roleInfoVO = encapsulateRoleByIdOrName(sqlUserDO.getRole(),"ruuid");
                if (roleInfoVO != null) {
                    roleInfoVoS.add(roleInfoVO);
                }
            }
        }

        // 根据用户名获取角色信息
        for (String username: usernames) {
            //  根据username获取用户信息
            SqlUserDO sqlUserDO = userDAO.getUserByUsername(username);
            if (sqlUserDO == null) {
                continue;
            }

            //  根据用户的ruuid查询角色
            if (sqlUserDO.getRole() != null || !sqlUserDO.getRole().isEmpty()) {
                //  封装数据
                RoleInfoVO roleInfoVO = encapsulateRoleByIdOrName(sqlUserDO.getRole(),"ruuid");
                if (roleInfoVO != null) {
                    roleInfoVoS.add(roleInfoVO);
                }
            }
        }

        // 根据角色id获取角色信息
        for (String ruuid: ruuids) {
            //  根据用户的ruuid查询角色
            if (ruuid != null || !ruuid.isEmpty()) {
                //  封装数据
                RoleInfoVO roleInfoVO = encapsulateRoleByIdOrName(ruuid,"ruuid");
                if (roleInfoVO != null) {
                    roleInfoVoS.add(roleInfoVO);
                }
            }
        }

        // 根据角色名称获取角色信息
        for (String roleName: roleNames) {
            //  根据用户的ruuid查询角色
            if (roleName != null || !roleName.isEmpty()) {
                //  封装数据
                RoleInfoVO roleInfoVO = encapsulateRoleByIdOrName(roleName,"roleName");
                if (roleInfoVO != null) {
                    roleInfoVoS.add(roleInfoVO);
                }
            }
        }

        return roleInfoVoS;
    }



    private final static String ROLE_TYPE_ROLE_NAME = "roleName";
    private final static String ROLE_TYPE_ROLE_ID = "ruuid";
    /**
     * ## 封装角色信息
     * 根据ruuid或角色名称查询到角色数据，然后进行封装
     *
     * @param data 要查询的数据
     * @param type 查询的类型（ruuid、roleName）
     * @return 角色信息实体类
     */
    private RoleInfoVO encapsulateRoleByIdOrName (String data,
                                                  String type) {
        //  查询角色数据
        SqlRoleDO sqlRoleDO = null;
        if (ROLE_TYPE_ROLE_NAME.equals(type)) {
            sqlRoleDO = roleDAO.getRoleByRUUID(data);
        } else if (ROLE_TYPE_ROLE_ID.equals(type)) {
            sqlRoleDO = roleDAO.getRoleByRoleName(data);
        }
        //  未查询，返回null
        if (sqlRoleDO == null) {
            return null;
        }
        //  封装数据
        RoleInfoVO roleInfoVO = new RoleInfoVO();
        Util.INSTANCE.copyPropertiesData(sqlRoleDO, roleInfoVO);
        if ( ! sqlRoleDO.getPermissions().equals(gson.toJson(new String[0])) ) {
            roleInfoVO.setPermissions(gson.fromJson(sqlRoleDO.getPermissions(),String[].class));
        } else {
            roleInfoVO.setPermissions(new String[0]);
        }

        return  roleInfoVO;
    }





}
