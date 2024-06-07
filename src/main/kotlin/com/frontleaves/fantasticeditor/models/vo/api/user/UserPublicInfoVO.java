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

package com.frontleaves.fantasticeditor.models.vo.api.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户基本信息
 * <p>
 *
 * @author zrx
 * @date 2024/5/31 21:33
 * @since v1.0.0
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserPublicInfoVO {
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

