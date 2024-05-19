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
 * ## 短信类型模板
 * 用于定义短信类型模板，包含短信类型名称和描述，用于短信服务接口，会显示在短信发送记录以及短信发送对方收信
 *
 * @since v1.0.0
 * @property typeName 短信类型名称
 * @property description 短信类型描述
 * @constructor 创建一个短信类型模板
 * @return SmsControl
 * @author xiao_lfeng
 */
enum class SmsControl(val typeName: String, val description: String) {
    USER_REGISTER("UserRegister", "注册账户"),
    CHECK_CODE("checkCode", "检查验证码"),
}
