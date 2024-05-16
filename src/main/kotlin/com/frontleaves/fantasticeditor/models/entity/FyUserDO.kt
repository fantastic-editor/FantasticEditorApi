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

package com.frontleaves.fantasticeditor.models.entity

import com.baomidou.mybatisplus.annotation.TableName
import java.sql.Timestamp

/**
 * # 用户实体类
 * 用于定义用户实体类；
 *
 * @since v1.0.0
 * @property uuid 用户唯一标识
 * @property username 用户名
 * @property email 邮箱
 * @property phone 手机号
 * @property password 密码
 * @property oldPassword 旧密码
 * @property avatar 头像
 * @property optAuth 操作权限
 * @property mailVerify 邮箱验证
 * @property phoneVerify 手机验证
 * @property basicInformation 基本信息
 * @property role 角色
 * @property vip 付费会员
 * @property createdAt 创建时间
 * @property updatedAt 更新时间
 * @constructor 创建一个用户实体类
 * @author xiao_lfeng
 */
@TableName("fy_user")
data class FyUserDO(
    val uuid: String,
    val username: String,
    val email: String,
    val phone: String,
    val password: String,
    val oldPassword: String? = null,
    val avatar: String? = null,
    val otpAuth: String? = null,
    val mailVerify: Boolean,
    val phoneVerify: Boolean,
    val basicInformation: String,
    val role: String,
    val vip: String? = null,
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp,
)
