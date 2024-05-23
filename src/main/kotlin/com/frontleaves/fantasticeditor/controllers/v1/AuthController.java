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

package com.frontleaves.fantasticeditor.controllers.v1;

import com.frontleaves.fantasticeditor.exceptions.library.CheckFailureException;
import com.frontleaves.fantasticeditor.models.dto.UserCurrentDTO;
import com.frontleaves.fantasticeditor.models.vo.api.AuthUserLoginVO;
import com.frontleaves.fantasticeditor.models.vo.api.AuthUserRegisterVO;
import com.frontleaves.fantasticeditor.services.interfaces.SmsService;
import com.frontleaves.fantasticeditor.services.interfaces.UserService;
import com.frontleaves.fantasticeditor.utility.BaseResponse;
import com.frontleaves.fantasticeditor.utility.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 授权控制器
 * <p>
 * 用于处理授权相关的请求, 如登录、注册等
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final SmsService smsService;
    private final UserService userService;

    /**
     * 用户登录
     * <p>
     * 用户登录，使用用户名和密码登录
     *
     * @param authUserLoginVO 登录信息
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Void>> userLogin(
            @RequestBody @Validated final AuthUserLoginVO authUserLoginVO
    ) {
        // 获取登录
        return ResultUtil.success("登录成功");
    }

    /**
     * 用户注册
     * <p>
     * 用户注册，使用用户名和密码注册。
     *
     * @param authUserRegisterVO 用户注册信息
     * @return 注册结果
     */
    @Transactional
    @PostMapping("/register")
    public ResponseEntity<BaseResponse<UserCurrentDTO>> userRegister(
            @RequestBody @Validated final AuthUserRegisterVO authUserRegisterVO
    ) {
        // 对验证码进行校验
        boolean verifyCode = smsService.checkCode(authUserRegisterVO.getPhone(), authUserRegisterVO.getVerifyCode());
        if (!verifyCode) {
            throw new CheckFailureException("手机验证码错误");
        }
        // 注册用户
        UserCurrentDTO getUserCurrent = userService.userRegister(authUserRegisterVO);
        return ResultUtil.success("注册成功", getUserCurrent);
    }

    /**
     * 发送注册短信验证码
     * <p>
     * 发送注册短信验证码；用于用户注册时的手机验证码
     *
     * @return 发送结果
     */
    @Transactional
    @GetMapping("/register/sms")
    public ResponseEntity<BaseResponse<Void>> sendRegisterSmsCode(
            @NotNull @RequestParam final String phone
    ) {
        if (!phone.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$")) {
            throw new CheckFailureException("手机号格式错误");
        }
        // 发送验证码
        userService.sendRegisterPhoneCode(phone);
        return ResultUtil.success("发送成功");
    }
}
