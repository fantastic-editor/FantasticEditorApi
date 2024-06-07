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
 * 用户实体类
 * <p>
 * 用于定义用户实体类；
 *
 * @author xiao_lfeng
 * @since v1.0.0
 */
@Data
@NoArgsConstructor
@TableName("fy_user")
@Accessors(chain = true)
public class SqlUserDO {
	public String uuid;
	public String username;
	public String email;
	public String phone;
	public String password;
	public String oldPassword;
	public String avatar;
	public String otpAuth;
	public Boolean mailVerify;
	public Boolean phoneVerify;
	public String basicInformation;
	public String role;
	public String vip;
	public Timestamp createdAt;
	public Timestamp updatedAt;
}

