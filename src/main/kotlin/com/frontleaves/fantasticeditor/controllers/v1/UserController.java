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

import com.frontleaves.fantasticeditor.exceptions.BusinessException;
import com.frontleaves.fantasticeditor.models.vo.api.user.UserMailVerifyVO;
import com.frontleaves.fantasticeditor.models.vo.api.user.UserPublicInfoVO;
import com.frontleaves.fantasticeditor.services.interfaces.UserService;
import com.frontleaves.fantasticeditor.utility.BaseResponse;
import com.frontleaves.fantasticeditor.utility.ErrorCode;
import com.frontleaves.fantasticeditor.utility.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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

    /**
     * 重发邮件验证码
     * <hr>
     * 用于用户在注册时，未收到验证码或者验证码失效时，重新发送验证码；
     *
     * @param email 邮箱地址
     * @return 重发结果
     */
    @GetMapping("/mail/resend")
    public ResponseEntity<BaseResponse<Void>> authResendMailVerify(
            @RequestParam("email") final String email
    ) {
        if (email.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
            userService.sendMailVerify(email);
            return ResultUtil.success("发送成功");
        } else {
            throw new BusinessException("邮箱格式错误", ErrorCode.REQUEST_BODY_PARAMETERS_ERROR);
        }
    }


    /**
     * 根据uuid、用户名、手机、邮箱进行查询用户的基本信息
     * <hr>
     * 根据传入的uuid、用户名、手机号、邮箱数组查询用户基本信息
     * <li>uuids、usernames、phones、emails参数可为空</li>
     *
     * @param uuids 用户uuid数组
     * @param usernames 用户名数组
     * @param phones 用户手机号数组
     * @param emails 用户邮箱数组
     * @return 用户基本信息
     */
    @GetMapping("/info/get/profile")
    public ResponseEntity<BaseResponse<List<UserPublicInfoVO>>> getUserProfileInfo(
        @RequestParam(required = false, defaultValue = "") List<String> uuids,
        @RequestParam(required = false, defaultValue = "") List<String> usernames,
        @RequestParam(required = false, defaultValue = "") List<String> phones,
        @RequestParam(required = false, defaultValue = "") List<String> emails
    ) {

        // 检查电话号码和电子邮件地址的格式
        boolean allPhonesValid = phones.stream().allMatch(phone ->
                Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", phone));
        if (!allPhonesValid) {
            throw new BusinessException("手机号格式错误", ErrorCode.REQUEST_PARAMETERS_ERROR);
        }
        boolean allEmailsValid = emails.stream().allMatch(email ->
                Pattern.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", email));
        if (!allEmailsValid) {
            throw new BusinessException("邮箱格式错误", ErrorCode.REQUEST_PARAMETERS_ERROR);
        }

        //获取用户基本信息
        List<UserPublicInfoVO> userPublicInfoVOS =
                userService.getUserProfileInfos(uuids, usernames, phones, emails);

        return ResultUtil.success("获取用户基本信息成功", userPublicInfoVOS);
    }


}
