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

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.frontleaves.fantasticeditor.constant.MailTemplateEnum;
import com.frontleaves.fantasticeditor.constant.SmsControl;
import com.frontleaves.fantasticeditor.dao.RoleDAO;
import com.frontleaves.fantasticeditor.dao.UserDAO;
import com.frontleaves.fantasticeditor.dao.VipDAO;
import com.frontleaves.fantasticeditor.exceptions.BusinessException;
import com.frontleaves.fantasticeditor.models.dto.UserCurrentDTO;
import com.frontleaves.fantasticeditor.models.entity.redis.RedisMailCodeDO;
import com.frontleaves.fantasticeditor.models.entity.redis.RedisSmsCodeDO;
import com.frontleaves.fantasticeditor.models.entity.redis.RedisUserPublicInfoDO;
import com.frontleaves.fantasticeditor.models.entity.sql.SqlRoleDO;
import com.frontleaves.fantasticeditor.models.entity.sql.SqlUserDO;
import com.frontleaves.fantasticeditor.models.entity.sql.SqlVipDO;
import com.frontleaves.fantasticeditor.models.vo.api.auth.AuthUserLoginVO;
import com.frontleaves.fantasticeditor.models.vo.api.auth.AuthUserRegisterVO;
import com.frontleaves.fantasticeditor.models.vo.api.user.UserPublicInfoVO;
import com.frontleaves.fantasticeditor.services.interfaces.MailService;
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
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private final VipDAO vipDAO;
    private final RedisUtil redisUtil;
    private final OperateUtil operateUtil;
    private final SmsService smsService;
    private final MailService mailService;

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
    public UserCurrentDTO userRegister(final @NotNull AuthUserRegisterVO authUserRegisterVO) {
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
    public void sendRegisterPhoneCode(final @NotNull String phone) {
        // 对手机号内容进行检查，检查缓存中是否存在
        operateUtil.checkSmsResendAble(phone);
        // 检查此手机是否已被注册
        SqlUserDO getUser = userDAO.getUserByPhone(phone);
        if (getUser != null) {
            throw new BusinessException("手机号已被注册", ErrorCode.USER_EXIST);
        }
        // 新建验证码并存入缓存
        String getNumberCode = Util.INSTANCE.generateNumber(6);
        RedisSmsCodeDO smsPhoneDO = new RedisSmsCodeDO()
                .setPhone(phone)
                .setCode(getNumberCode)
                .setSendAt(String.valueOf(System.currentTimeMillis()))
                .setFrequency("1");
        RedisSmsCodeDO getPhoneCode = Util.INSTANCE.mapToObject(
                redisUtil.hashGet("sms:code:" + phone),
                RedisSmsCodeDO.class
        );
        if (getPhoneCode != null) {
            smsPhoneDO.setFrequency(String.valueOf(Long.parseLong(getPhoneCode.getFrequency()) + 1));
        }
        if (redisUtil.hashSet("sms:code:" + phone, smsPhoneDO, 15 * 60)) {
            // 发送验证码
            if (!smsService.sendCode(phone, getNumberCode, SmsControl.USER_REGISTER)) {
                throw new BusinessException("发送失败", ErrorCode.VERIFY_CODE_ERROR);
            }
        } else {
            throw new BusinessException("发送失败", ErrorCode.VERIFY_CODE_ERROR);
        }
    }

    @Override
    public boolean userLogin(@NotNull final AuthUserLoginVO authUserLoginVO) {
        // 定义邮箱的正则表达式
        String emailRegex =
                "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        // 定义手机号的正则表达式
        String phoneRegex = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
        Pattern phonePattern = Pattern.compile(phoneRegex);
        // 先判断传入的username为什么类型
        String username = authUserLoginVO.getUsername();
        Matcher emailMatcher = emailPattern.matcher(username);
        Matcher phoneMatcher = phonePattern.matcher(username);
        if (emailMatcher.matches()) {
            SqlUserDO sqlUserDO = userDAO.getUserByEmail(username);
            if (sqlUserDO == null) {
                return false;
            }
            if (!sqlUserDO.getMailVerify()) {
                throw new BusinessException("邮箱未验证", ErrorCode.OPERATION_FAILED);
            }
            if (!Util.INSTANCE.verifyPassword(authUserLoginVO.getPassword(), sqlUserDO.getPassword())) {
                throw new BusinessException("邮箱或密码错误", ErrorCode.OPERATION_FAILED);
            }
            return true;
        } else if (phoneMatcher.matches()) {
            SqlUserDO sqlUserDO = userDAO.getUserByPhone(username);
            if (sqlUserDO == null) {
                return false;
            }
            if (!Util.INSTANCE.verifyPassword(authUserLoginVO.getPassword(), sqlUserDO.getPassword())) {
                throw new BusinessException("手机号或密码错误", ErrorCode.OPERATION_FAILED);
            }
            return true;
        } else {
            SqlUserDO sqlUserDO = userDAO.getUserByUsername(username);
            if (sqlUserDO == null) {
                return false;
            }
            if (!Util.INSTANCE.verifyPassword(authUserLoginVO.getPassword(), sqlUserDO.getPassword())) {
                throw new BusinessException("用户名或密码错误", ErrorCode.OPERATION_FAILED);
            }
            return true;
        }
    }

    @Override
    @Transactional
    public void checkMailVerify(@NotNull final String email, @NotNull final String verifyCode) {
        // 获取验证码
        RedisMailCodeDO getMailCode = Util.INSTANCE.mapToObject(
                redisUtil.hashGet("mail:code:" + email),
                RedisMailCodeDO.class
        );
        if (getMailCode == null) {
            throw new BusinessException("验证码不存在", ErrorCode.VERIFY_CODE_ERROR);
        }
        // 检查验证码
        if (!getMailCode.getCode().equals(verifyCode)) {
            throw new BusinessException("验证码错误", ErrorCode.VERIFY_CODE_ERROR);
        }
        // 删除验证码
        redisUtil.delete("mail:code:" + email);
        // 检查用户是否已验证
        SqlUserDO getUser = userDAO.getUserByEmail(email);
        if (getUser == null) {
            throw new BusinessException("用户不存在", ErrorCode.USER_NOT_EXIST);
        }
        if (getUser.getMailVerify()) {
            throw new BusinessException("用户已验证", ErrorCode.OPERATION_FAILED);
        }
        // 验证成功
        getUser.setMailVerify(true)
                .setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        if (!userDAO.update(getUser, new UpdateWrapper<SqlUserDO>().eq("uuid", getUser.getUuid()))) {
            throw new BusinessException("验证失败", ErrorCode.OPERATION_FAILED);
        }
    }

    @Override
    public void sendMailVerify(@NotNull final String email) {
        // 检查邮箱是否已被注册
        SqlUserDO getUser = userDAO.getUserByEmail(email);
        if (getUser == null) {
            throw new BusinessException("邮箱未注册", ErrorCode.USER_NOT_EXIST);
        }
        // 检查邮箱是否已验证
        if (getUser.getMailVerify()) {
            throw new BusinessException("邮箱已验证", ErrorCode.OPERATION_FAILED);
        }
        // 检查前一次所发送的验证码是否已过期或达到可重新发送的标准
        RedisMailCodeDO getMailCode = Util.INSTANCE.mapToObject(
                redisUtil.hashGet("mail:code:" + email),
                RedisMailCodeDO.class
        );
        // 生成验证码
        String getNumberCode = Util.INSTANCE.generateNumber(6);
        if (getMailCode != null) {
            if (System.currentTimeMillis() - Long.parseLong(getMailCode.getSendAt()) < 60 * 1000) {
                throw new BusinessException("发送过于频繁", ErrorCode.OPERATION_FAILED);
            }
        }
        // 发送验证码
        mailService.sendVerifyCodeMail(email, getNumberCode, MailTemplateEnum.USER_REGISTER);
    }


    @Override
    public UserPublicInfoVO getMyUserProfileInfo(@NotNull String uuid) {

        UserPublicInfoVO userPublicInfoVO = new UserPublicInfoVO();

        //  redis查询的uuid
        String redisKey = "user:uuid:"+uuid;

        //  先从redis中查询 用户公开信息实体类
        RedisUserPublicInfoDO getUserPInfoFromRedis =
                Util.INSTANCE.mapToObject(
                        redisUtil.hashGet(redisKey),
                        RedisUserPublicInfoDO.class
                );

        //   如果redis数据不为空，返回redis的数据
        if (getUserPInfoFromRedis != null) {
            Util.INSTANCE.copyPropertiesData(getUserPInfoFromRedis, userPublicInfoVO);
        }

        //  如果redis数据为空，则进行封装，并将数据存储到redis中
        //  从数据库获取User数据
        SqlUserDO userDO = userDAO.getUserByUUID(uuid);
        //  复制属性相同的数据
        Util.INSTANCE.copyPropertiesData(userDO, userPublicInfoVO);
        //  设置用户Vip名称
        SqlVipDO vipDO = null;
        if (userDO.getVip() != null) {
            vipDO = vipDAO.getVipByVUUID(userDO.getVip());
        }
        userPublicInfoVO.setVipName
                ((vipDO != null && vipDO.getName() != null) ? vipDO.getName() : "");
        //  设置用户角色名称
        SqlRoleDO roleDO = roleDAO.getRoleByRUUID(userDO.getRole());
        userPublicInfoVO.setRoleName
                ((roleDO != null && roleDO.getName() != null) ? roleDO.getName() : "");

        //  设置redis数据
        redisUtil.setHasKey(redisKey, Util.INSTANCE.objectToMap(userPublicInfoVO));

        return  userPublicInfoVO;
    }


    /**
     * 封装用户开放信息VO类
     * <p>
     * 将用户sql实体类封装为用户开放信息VO类，并根据搜索类型将结果储存到redis中
     *
     * @param searchType 搜索类型(uuid、username、phone、email)
     * @param searchData 搜索数据
     * @return 用户开发信息实体类
     */
    private UserPublicInfoVO searchUserPublicInfo(final String searchType,
                                                  final String searchData) {

        //  拼接redis的key
        String redisKey = "userPInfo:"+searchType+":"+searchData;

        //  定义用户公共信息类实体
        UserPublicInfoVO userVO = new UserPublicInfoVO();

        //  先从redis中查询 用户开发信息实体类
        RedisUserPublicInfoDO getUserPInfoFromRedis =
                Util.INSTANCE.mapToObject(
                        redisUtil.hashGet(redisKey),
                        RedisUserPublicInfoDO.class
                );

        //  如果redis没有数据、则进行封装，并将结果存储到redis中
        if (getUserPInfoFromRedis == null) {
            //  获取用户sql实体类
            SqlUserDO userDO = switch (searchType) {
                case "uuid" -> userDAO.getUserByUUID(searchData);
                case "username" -> userDAO.getUserByUsername(searchData);
                case "phone" -> userDAO.getUserByPhone(searchData);
                case "email" -> userDAO.getUserByEmail(searchData);
                default -> null;
            };
            //  如果用户sql实体类为空，返回null
            if (userDO == null) {
                return null;
            }
            //  进行封装
            Util.INSTANCE.copyPropertiesData(userDO, userVO);
            //  设置用户Vip名称
            SqlVipDO vipDO = null;
            if (userDO.getVip() != null) {
                vipDO = vipDAO.getVipByVUUID(userDO.getVip());
            }

            userVO.setVipName((vipDO != null && vipDO.getDisplayName() != null) ? vipDO.getDisplayName() : "");
            userVO.setVipId((vipDO != null && vipDO.getVuuid() != null) ? vipDO.getVuuid() : "");
            //  设置用户角色名称与id
            SqlRoleDO roleDO = roleDAO.getRoleByRUUID(userDO.getRole());
            userVO.setRoleName((roleDO != null && roleDO.getDisplayName() != null) ? roleDO.getDisplayName() : "");
            userVO.setRoleId((roleDO != null && roleDO.getRuuid() != null) ? roleDO.getRuuid() : "");

            redisUtil.setHasKey(redisKey, Util.INSTANCE.objectToMap(userVO));
        }

        //  如果查询到了数据，返回结果即可
        if (getUserPInfoFromRedis != null) {
            Util.INSTANCE.copyPropertiesData(getUserPInfoFromRedis, userVO);
        }

        return userVO;
    }
}
