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

package com.frontleaves.fantasticeditor.models.vo.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册VO
 * <p>
 * 用于接收用户注册信息
 *
 * @since v1.0.0
 * @version v1.0.0
 * @author xiao_lfeng
 */
@Data
@NoArgsConstructor
public class AuthUserRegisterVO {
    @Pattern(regexp = "^[0-9A-Za-z-_]+$", message = "用户名格式不正确，用户名仅允许数字密码以及“-”和“_”组合")
    public String username;

    @Pattern(regexp = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$", message = "请输入正确的邮箱格式")
    public String email;

    @Pattern(
            regexp = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$",
            message = "请输入正确的手机号"
    )
    public String phone;

    @NotBlank(message = "密码不能为空")
    public String password;

    @Pattern(regexp = "^[0-9A-Za-z]{6,10}", message = "请输入正确的验证码格式")
    public String verifyCode;
}
