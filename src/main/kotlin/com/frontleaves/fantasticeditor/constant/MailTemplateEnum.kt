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

package com.frontleaves.fantasticeditor.constant

/**
 * ## 邮件模板枚举类
 * 用于定义邮件模板，包含邮件模板的描述和模板名称
 *
 * @since v1.0.0
 * @property description 邮件模板描述
 * @property template 邮件模板名称
 * @property subject 邮件主题
 * @property hasCode 是否为验证码
 * @author xiao_lfeng
 */
enum class MailTemplateEnum(val subject: String, val template: String, val description: String, val hasCode: Boolean) {
    USER_REGISTER("用户注册", "user-register", "用户进行注册时候所发送的内容", true),
    USER_LOGIN("用户登录", "user-login", "用户进行登录时候所发送的内容", true),
    USER_EDIT_PASSWORD("用户修改密码", "user-edit-password", "用户进行修改密码时候所发送的内容", true),
}
