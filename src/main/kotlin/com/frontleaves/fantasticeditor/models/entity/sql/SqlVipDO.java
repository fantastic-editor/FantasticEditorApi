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

package com.frontleaves.fantasticeditor.models.entity.sql;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * 付费会员实体类
 * <p>
 * 用于定义付费会员实体类；
 *
 * @author xiao_lfeng
 * @since v1.0.0
 */
@Data
@NoArgsConstructor
@TableName("fy_vip")
@Accessors(chain = true)
public class SqlVipDO {
	public String vuuid;
	public String name;
	public String displayName;
	public Long price;
	public String description;
	public Timestamp updatedAt;
}

