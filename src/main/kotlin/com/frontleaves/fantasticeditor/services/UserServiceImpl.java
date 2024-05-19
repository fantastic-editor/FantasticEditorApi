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

import com.frontleaves.fantasticeditor.constant.SmsControl;
import com.frontleaves.fantasticeditor.dao.RoleDAO;
import com.frontleaves.fantasticeditor.dao.UserDAO;
import com.frontleaves.fantasticeditor.exceptions.BusinessException;
import com.frontleaves.fantasticeditor.models.dto.UserCurrentDTO;
import com.frontleaves.fantasticeditor.models.entity.cache.RedisSmsPhoneDO;
import com.frontleaves.fantasticeditor.models.entity.sql.SqlRoleDO;
import com.frontleaves.fantasticeditor.models.entity.sql.SqlUserDO;
import com.frontleaves.fantasticeditor.models.vo.api.AuthUserRegisterVO;
import com.frontleaves.fantasticeditor.services.interfaces.SmsService;
import com.frontleaves.fantasticeditor.services.interfaces.UserService;
import com.frontleaves.fantasticeditor.utility.ErrorCode;
import com.frontleaves.fantasticeditor.utility.OperateUtil;
import com.frontleaves.fantasticeditor.utility.Util;
import com.frontleaves.fantasticeditor.utility.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现
 * <p>
 * 用于处理用户相关的业务逻辑
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @see UserService
 * @since v1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final RedisUtil redisUtil;
    private final OperateUtil operateUtil;
    private final SmsService smsService;

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
        SqlUserDO getUser = userDAO.getUserByUsername(authUserRegisterVO.getUsername());
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
        SqlRoleDO getRole = roleDAO.getRoleByRoleName("user");
        assert getRole != null;
        assert getRole.getRuuid() != null;
        // 创建用户
        SqlUserDO user = new SqlUserDO()
                .setUuid(Util.INSTANCE.makeNoDashUUID())
                .setUsername(authUserRegisterVO.getUsername())
                .setEmail(authUserRegisterVO.getEmail())
                .setPhone(authUserRegisterVO.getPhone())
                .setPassword(Util.INSTANCE.encryptPassword(authUserRegisterVO.getPassword()))
                .setOtpAuth(Util.INSTANCE.makeNoDashUUID())
                .setMailVerify(false)
                .setPhoneVerify(true)
                .setBasicInformation("{1=1}")
                .setRole(getRole.getRuuid());
        if (userDAO.save(user)) {
            UserCurrentDTO getUserCurrent = new UserCurrentDTO();
            BeanUtils.copyProperties(user, getUserCurrent);
            return getUserCurrent;
        } else {
            throw new BusinessException("用户注册失败", ErrorCode.OPERATION_FAILED);
        }
    }

    @Override
    public void sendRegisterPhoneCode(@NotNull String phone) {
        // 对手机号内容进行检查，检查缓存中是否存在
        operateUtil.checkSmsResendAble(phone);
        // 检查此手机是否已被注册
        SqlUserDO getUser = userDAO.getUserByPhone(phone);
        if (getUser != null) {
            throw new BusinessException("手机号已被注册", ErrorCode.USER_EXIST);
        }
        // 新建验证码并存入缓存
        String getNumberCode = Util.INSTANCE.generateNumber(6);
        RedisSmsPhoneDO smsPhoneDO = new RedisSmsPhoneDO()
                .setPhone(phone)
                .setCode(getNumberCode)
                .setSendAt(String.valueOf(System.currentTimeMillis()))
                .setFrequency("1");
        RedisSmsPhoneDO getPhoneCode = Util.INSTANCE.mapToObject(redisUtil.hashGet("sms:code:" + phone), RedisSmsPhoneDO.class);
        if (getPhoneCode != null) {
            smsPhoneDO.setFrequency(String.valueOf(Long.parseLong(getPhoneCode.getFrequency()) + 1));
        }
        if (redisUtil.hashSet("sms:code:" + phone, smsPhoneDO, 15 * 60)) {
            // 发送验证码
            if (!smsService.sendCode(phone, getNumberCode, SmsControl.USER_REGISTER)) {
                throw new BusinessException("发送失败", ErrorCode.OPERATION_FAILED);
            }
        } else {
            throw new BusinessException("发送失败", ErrorCode.OPERATION_FAILED);
        }
    }
}
