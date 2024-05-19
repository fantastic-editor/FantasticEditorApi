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

package com.frontleaves.fantasticeditor.mappers

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.frontleaves.fantasticeditor.models.entity.sql.SqlUserDO
import org.apache.ibatis.annotations.Mapper

/**
 * # 用户映射器
 * 用于定义用户映射器；
 *
 * @since v1.0.0
 * @constructor 创建一个用户映射器
 * @author xiao_lfeng
 */
@Mapper
interface UserMapper : BaseMapper<SqlUserDO>
