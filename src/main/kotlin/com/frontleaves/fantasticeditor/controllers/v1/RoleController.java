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
import com.frontleaves.fantasticeditor.models.vo.api.RoleCustomEditVO;
import com.frontleaves.fantasticeditor.services.interfaces.RoleService;
import com.frontleaves.fantasticeditor.utility.BaseResponse;
import com.frontleaves.fantasticeditor.utility.ErrorCode;
import com.frontleaves.fantasticeditor.utility.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 角色控制器
 * <hr/>
 * 角色控制器，用于处理角色相关的请求；
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/role")
public class RoleController {
    private final RoleService roleService;

    /**
     * 修改自定义角色
     * <hr/>
     * 获取角色列表，用于获取所有角色的信息；
     *
     * @return 角色列表
     */
    @PostMapping("/{roleId}")
    public ResponseEntity<BaseResponse<Void>> editCustomRole(
            @NotNull @RequestBody final RoleCustomEditVO roleCustomEditVO,
            @NotNull @PathVariable final String roleId
    ) {
        // 检查 roleId 是否为不带破折号的 UUID
        if (!roleId.matches("^[a-fA-F0-9]{32}$")) {
            throw new BusinessException("角色ID格式错误", ErrorCode.REQUEST_PATH_ERROR);
        }
        // 对权限进行正则表达式检查
        for (String permission : roleCustomEditVO.permissions) {
            if (!permission.matches("^[a-zA-Z]+:[a-zA-Z]+$")) {
                throw new BusinessException("权限格式错误", ErrorCode.REQUEST_BODY_PARAMETERS_ERROR);
            }
        }
        if (roleService.editCustomRole(roleCustomEditVO, roleId)) {
            return ResultUtil.success("修改角色成功");
        } else {
            throw new BusinessException("修改角色失败", ErrorCode.OPERATION_FAILED);
        }
    }
}
