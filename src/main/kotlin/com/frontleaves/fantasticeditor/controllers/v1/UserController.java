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

import com.frontleaves.fantasticeditor.models.vo.api.user.UserMailVerifyVO;
import com.frontleaves.fantasticeditor.services.interfaces.UserService;
import com.frontleaves.fantasticeditor.utility.BaseResponse;
import com.frontleaves.fantasticeditor.utility.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制器
 * <hr>
 * 用户控制器，用于处理用户相关的请求;
 *
 * @since v1.0.0
 * @version v1.0.0
 * @author xiao_lfeng
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    /**
     * 邮箱验证
     * <hr>
     * 用户邮箱验证的地址，用于验证用户的邮箱是否合法；邮箱验证码的发送会根据用户在注册后，执行 authResendMailVerify 方法时的邮箱地址发送；
     * 验证码的有效时间为 15 分钟；获取到的验证码在当前接口进行验证；
     *
     * @return 注册结果
     */
    @PostMapping("/mail/verify")
    public ResponseEntity<BaseResponse<Void>> userMailVerify(
            @RequestBody @Validated final UserMailVerifyVO userMailVerifyVO
    ) {
        userService.checkMailVerify(userMailVerifyVO.getEmail(), userMailVerifyVO.getVerifyCode());
        return ResultUtil.success("验证成功");
    }
}
