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

package com.frontleaves.fantasticeditor.services

import com.baidubce.services.sms.SmsClient
import com.baidubce.services.sms.model.SendMessageV3Request
import com.frontleaves.fantasticeditor.constant.BaseDataConstant
import com.frontleaves.fantasticeditor.constant.SMSControl
import com.frontleaves.fantasticeditor.models.vo.SmsContentVO
import com.frontleaves.fantasticeditor.services.interfaces.SmsService
import com.frontleaves.fantasticeditor.utility.redis.RedisUtil
import org.springframework.stereotype.Service

/**
 * ## 短信服务实现
 * 用于发送短信验证码
 *
 * @since v1.0.0
 * @constructor 创建一个短信服务实现
 * @return Boolean
 * @author xiao_lfeng
 */
@Service
class SmsServiceImpl(
    private val redisUtil: RedisUtil,
    private val smsService: SmsClient,
) : SmsService {

    /**
     * ## 发送验证码
     * 需要指定的手机号以及验证码，用于验证；经过缓存验证后，若检查通过返回真，否则返回假
     *
     * @param phone 手机号
     * @param code 验证码
     * @param type 短信类型
     * @return Boolean
     */
    override fun sendCode(phone: String, code: String, type: SMSControl): Boolean {
        if (!phone.matches(Regex("^1[3-9]\\d{9}$"))) {
            throw IllegalArgumentException("手机号格式错误")
        }
        redisUtil.set("sms:code:$phone", code, 900L)
        val smsRequest = SendMessageV3Request().let { request ->
            request.also {
                it.mobile = phone
                it.template = BaseDataConstant.BCE_SMS_TEMPLATE_ID
                it.signatureId = BaseDataConstant.BCE_SMS_SIGNATURE_ID
                it.contentVar = HashMap<String, String>().also { map ->
                    val getClass = SmsContentVO().apply {
                        this.contactPerson = ""
                        this.typeName = type.typeName
                        this.code = code
                        this.service = BaseDataConstant.SERVICE_TITLE
                    }.javaClass
                    getClass.declaredFields.forEach { field ->
                        field.isAccessible = true
                        map[field.name] = field[getClass].toString()
                    }
                }
            }
        }
        val smsResponse = smsService.sendMessage(smsRequest)
        return smsResponse != null && smsResponse.isSuccess
    }

    /**
     * ## 检查验证码
     * 需要指定的手机号以及验证码，用于验证；经过缓存验证后，若检查通过返回真，否则返回假
     *
     * @param phone 手机号
     * @param verifyCode 验证码
     * @return Boolean
     */
    override fun checkCode(phone: String?, verifyCode: String?): Boolean {
        return takeIf { phone.isNullOrBlank() || verifyCode.isNullOrBlank() }?.let { false }
            ?: run {
                redisUtil.get("sms:code:$phone").takeIf { it == verifyCode }?.let {
                    redisUtil.delete("sms:$phone")
                    true
                } ?: run { false }
            }
    }
}
