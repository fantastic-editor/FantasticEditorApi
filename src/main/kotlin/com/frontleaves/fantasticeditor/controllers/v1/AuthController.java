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

import com.frontleaves.fantasticeditor.annotations.Slf4j;
import com.frontleaves.fantasticeditor.utility.BaseResponse;
import com.frontleaves.fantasticeditor.utility.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限控制器
 * <p>
 * 用于处理权限相关的请求, 如登录、注册等
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
@Slf4j
@RequiredArgsConstructor
@RestController("/api/v1/auth")
public class AuthController {

    /**
     * 用户登录
     * <p>
     * 用户登录，使用用户名和密码登录
     *
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Void>> userLogin() {
        return ResultUtil.success("登录成功");
    }
}