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
import com.frontleaves.fantasticeditor.annotations.KSlf4j.Companion.log
import com.frontleaves.fantasticeditor.constant.BaseDataConstant
import com.frontleaves.fantasticeditor.constant.BceDataConstant
import com.frontleaves.fantasticeditor.constant.SmsControl
import com.frontleaves.fantasticeditor.models.vo.SmsContentVO
import com.frontleaves.fantasticeditor.services.interfaces.SmsService
import com.frontleaves.fantasticeditor.utility.redis.RedisUtil
import com.google.gson.Gson
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
    private val smsClient: SmsClient,
    private val gson: Gson,
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
    override fun sendCode(phone: String, code: String, type: SmsControl): Boolean {
        if (!phone.matches(
                Regex("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}\$"),
            )
        ) {
            throw IllegalArgumentException("手机号格式错误")
        }
        val smsRequest = SendMessageV3Request().let { request ->
            request.also { it ->
                it.mobile = phone
                it.template = BceDataConstant.bceSmsTemplateID
                it.signatureId = BceDataConstant.bceSmsSignatureID
                it.contentVar = HashMap<String, String>().also { map ->
                    val getSmsContent = SmsContentVO().also { sms ->
                        sms.contactPerson = ""
                        sms.typeName = type.description
                        sms.code = code
                        sms.service = BaseDataConstant.SERVICE_TITLE
                    }
                    val getObject = gson.toJsonTree(getSmsContent).asJsonObject
                    getObject.keySet().forEach { map[it] = getObject[it].asString }
                }
            }
        }
        log.info("发送短信验证码：${BceDataConstant.bceSmsSignatureID}")
        val smsResponse = smsClient.sendMessage(smsRequest)
        log.info("发送短信验证码：$smsResponse")
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
