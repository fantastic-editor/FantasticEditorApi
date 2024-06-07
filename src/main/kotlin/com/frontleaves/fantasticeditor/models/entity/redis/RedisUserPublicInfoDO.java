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

package com.frontleaves.fantasticeditor.models.entity.redis;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Redis用户基本信息实体
 * <p>
 * 用于存储用户基本信息的实体
 * @since v1.0.0
 * @version v1.0.0
 * @author zrx
 * @date 2024/6/1 8:45
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class RedisUserPublicInfoDO {

	public String uuid;

	public String username;

	public String email;

	public String phone;

	public String basicInformation;

	public String roleId;

	public String roleName;

	public String vipId;

	public String vipName;

}


