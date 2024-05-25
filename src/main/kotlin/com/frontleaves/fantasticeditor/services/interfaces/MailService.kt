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

package com.frontleaves.fantasticeditor.services.interfaces

import com.frontleaves.fantasticeditor.constant.MailTemplateEnum

/**
 * ## 邮件服务接口
 * 用于发送邮件, 邮件模板由 [MailTemplateEnum] 枚举类定义
 *
 * @see MailTemplateEnum
 * @since v1.0.0
 * @author xiao_lfeng
 */
interface MailService {
    /**
     * ## 发送邮件
     * 发送邮件, 邮件模板由 [MailTemplateEnum] 枚举类定义, 模板参数由 [parameters] 指定; 邮件地址由 [email] 指定;
     * 根据模板和参数生成邮件内容, 并发送邮件
     *
     * @param email 邮件地址
     * @param template 邮件模板
     * @param parameters 模板参数
     */
    fun sendMail(email: String, template: MailTemplateEnum, parameters: Map<String, String?>)

    /**
     * ## 发送验证码邮件
     * 发送验证码邮件, 邮件模板由 [MailTemplateEnum] 枚举类定义, 验证码由 [verifyCode] 指定; 邮件地址由 [email] 指定;
     * 根据模板和验证码生成邮件内容, 并发送邮件；该接口将会调用 [sendMail] 接口进行发送邮件；并且将会生成验证码存储在 redis 缓存中；
     * 验证码有效时间为十五分钟，无需进一步设置 Redis 缓存操作。
     *
     * @param email 邮件地址
     * @param verifyCode 验证码
     * @param template 邮件模板
     */
    fun sendVerifyCodeMail(email: String, verifyCode: String, template: MailTemplateEnum)
}
