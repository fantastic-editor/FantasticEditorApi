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
import com.frontleaves.fantasticeditor.models.dto.GetPermissionDTO;
import com.frontleaves.fantasticeditor.models.entity.sql.SqlPermissionDO;
import com.frontleaves.fantasticeditor.services.interfaces.PermissionService;
import com.frontleaves.fantasticeditor.utility.BaseResponse;
import com.frontleaves.fantasticeditor.utility.ErrorCode;
import com.frontleaves.fantasticeditor.utility.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限控制器
 * <p>
 * 用于处理权限相关的请求, 如获取权限列表等
 *
 * @author DC_DC
 * @version v1.0.0
 * @since v1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/permission")
public class PermissionController {
    private final PermissionService permissionService;

    /**
     * 获取权限列表
     * <p>
     * 获取权限列表
     *
     * @param search 搜索关键字
     * @param page   页码
     * @param size   每页大小
     * @return 权限列表
     */
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<List<GetPermissionDTO>>> getPermissionList(
            @RequestParam(value = "search", required = false) final String search,
            @RequestParam(value = "page", defaultValue = "1") final String page,
            @RequestParam(value = "size", defaultValue = "20") final String size
    ) {
        // 对输入进行检查
        if (!page.matches("^[0-9]+$")) {
            throw new BusinessException("页码必须为数字", ErrorCode.REQUEST_PARAMETERS_ERROR);
        }
        if (!size.matches("^[0-9]+$")) {
            throw new BusinessException("每页大小必须为数字", ErrorCode.REQUEST_PARAMETERS_ERROR);
        }
        // 调用权限服务获取权限列表
        List<SqlPermissionDO> sqlPermissionDOList = permissionService.getPermissionList(
                search,
                Integer.parseInt(page),
                Integer.parseInt(size)
        );
        List<GetPermissionDTO> getPermissionDTOList = new ArrayList<>();
        for (SqlPermissionDO sqlPermissionDO : sqlPermissionDOList) {
            GetPermissionDTO getPermissionDTO = new GetPermissionDTO();
            BeanUtils.copyProperties(sqlPermissionDO, getPermissionDTO);
            getPermissionDTOList.add(getPermissionDTO);
        }
        return ResultUtil.success("获取权限列表成功", getPermissionDTOList);
    }
}
