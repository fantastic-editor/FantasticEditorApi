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

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.frontleaves.fantasticeditor.dao.PermissionDAO;
import com.frontleaves.fantasticeditor.models.entity.sql.SqlPermissionDO;
import com.frontleaves.fantasticeditor.services.interfaces.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DC_DC
 * Date: 2024/5/23/19:49
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionDAO permissionDAO;

    @NotNull
    @Override
    public List<SqlPermissionDO> getPermissionList(
            @Nullable final String search,
            @Nullable final Integer page,
            @Nullable final Integer size
    ) {
        int page1 = 1;
        int size1 = 20;
        String search1 = "";
        if (page != null && size != null) {
            page1 = page;
            size1 = size;
        } else if (size != null) {
            size1 = size;
        } else if (page != null) {
            page1 = page;
        } else if (search != null) {
            search1 = search;
        }
        Page<SqlPermissionDO> sqlPermissionDOList = permissionDAO.getPermissionsBySearch(search1, page1, size1);
        if (sqlPermissionDOList == null) {
            throw new RuntimeException("没有找到任何权限");
        } else {
            return sqlPermissionDOList.getRecords();
        }
    }
}
