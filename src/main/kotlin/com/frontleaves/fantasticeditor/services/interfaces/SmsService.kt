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

import com.frontleaves.fantasticeditor.constant.SMSControl

/**
 * ## 短信服务接口
 * 用于发送短信验证码
 *
 * @since v1.0.0
 * @constructor 创建一个短信服务接口
 * @return Boolean
 * @author xiao_lfeng
 */
interface SmsService {

    /**
     * ## 发送验证码
     * 需要指定的手机号以及验证码，用于验证；经过缓存存储后，若短信发送成功返回真，否则返回假
     *
     * @param phone 手机号
     * @param code 验证码
     * @param type 短信类型
     * @return Boolean
     */
    fun sendCode(phone: String, code: String, type: SMSControl): Boolean

    /**
     * ## 检查验证码
     * 需要指定的手机号以及验证码，用于验证；经过缓存验证后，若检查通过返回真，否则返回假
     *
     * @param phone 手机号
     * @param verifyCode 验证码
     * @return Boolean
     */
    fun checkCode(phone: String?, verifyCode: String?): Boolean
}
