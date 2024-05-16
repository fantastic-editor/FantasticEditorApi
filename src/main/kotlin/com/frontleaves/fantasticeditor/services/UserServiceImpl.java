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
import com.frontleaves.fantasticeditor.models.dto.UserCurrentDTO;
import com.frontleaves.fantasticeditor.models.entity.FyRoleDO;
import com.frontleaves.fantasticeditor.models.entity.FyUserDO;
import com.frontleaves.fantasticeditor.models.vo.AuthUserRegisterVO;
import com.frontleaves.fantasticeditor.services.interfaces.UserService;
import com.frontleaves.fantasticeditor.utility.ErrorCode;
import com.frontleaves.fantasticeditor.utility.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现
 * <p>
 * 用于处理用户相关的业务逻辑
 *
 * @since v1.0.0
 * @version v1.0.0
 * @see UserService
 * @author xiao_lfeng
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;

    /**
     * 用户注册
     * <p>
     * 用户注册，使用用户名和密码注册
     *
     * @param authUserRegisterVO 用户注册信息
     * @return 注册结果
     */
    @NotNull
    @Override
    public UserCurrentDTO userRegister(@NotNull AuthUserRegisterVO authUserRegisterVO) {
        assert authUserRegisterVO.getUsername() != null;
        assert authUserRegisterVO.getEmail() != null;
        assert authUserRegisterVO.getPhone() != null;
        assert authUserRegisterVO.getPassword() != null;
        // 获取信息查询用户
        FyUserDO getUser = userDAO.getUserByUsername(authUserRegisterVO.getUsername());
        if (getUser == null) {
            getUser = userDAO.getUserByEmail(authUserRegisterVO.getEmail());
        }
        if (getUser == null) {
            getUser = userDAO.getUserByEmail(authUserRegisterVO.getPhone());
        }
        if (getUser != null) {
            throw new BusinessException("用户已存在", ErrorCode.USER_EXIST);
        }
        // 获取基本用户组
        FyRoleDO getRole = roleDAO.getRoleByRoleName("user");
        assert getRole != null;
        assert getRole.getRuuid() != null;
        // 创建用户
        FyUserDO user = new FyUserDO(
                Util.INSTANCE.makeUUID().toString().replace("-", ""),
                authUserRegisterVO.getUsername(),
                authUserRegisterVO.getEmail(),
                authUserRegisterVO.getPhone(),
                Util.INSTANCE.encryptPassword(authUserRegisterVO.getPassword()),
                null,
                null,
                Util.INSTANCE.makeUUID().toString().replace("-", ""),
                false,
                false,
                "{}",
                getRole.getRuuid(),
                null,
                null,
                null
        );
        if (userDAO.save(user)) {
            return Util.INSTANCE.copyProperties(user, UserCurrentDTO.class);
        } else {
            throw new BusinessException("用户注册失败", ErrorCode.OPERATION_FAILED);
        }
    }
}
