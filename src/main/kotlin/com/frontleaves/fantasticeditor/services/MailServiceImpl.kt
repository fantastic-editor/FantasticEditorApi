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

import com.frontleaves.fantasticeditor.annotations.KSlf4j.Companion.log
import com.frontleaves.fantasticeditor.constant.BaseDataConstant
import com.frontleaves.fantasticeditor.constant.MailTemplateEnum
import com.frontleaves.fantasticeditor.exceptions.BusinessException
import com.frontleaves.fantasticeditor.exceptions.library.MailTemplateNotFoundException
import com.frontleaves.fantasticeditor.models.entity.redis.RedisMailCodeDO
import com.frontleaves.fantasticeditor.services.interfaces.MailService
import com.frontleaves.fantasticeditor.utility.ErrorCode
import com.frontleaves.fantasticeditor.utility.Util
import com.frontleaves.fantasticeditor.utility.redis.RedisUtil
import jakarta.mail.MessagingException
import jakarta.mail.internet.MimeMessage
import org.springframework.core.io.ClassPathResource
import org.springframework.mail.MailSendException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine

/**
 * ## 邮件服务实现类
 * 用于发送邮件, 邮件模板由 [MailTemplateEnum] 枚举类定义
 *
 * @see MailTemplateEnum
 * @since v1.0.0
 * @author xiao_lfeng
 */
@Service
class MailServiceImpl(
    private val javaMailSender: JavaMailSender,
    private val templateEngine: SpringTemplateEngine,
    private val redisUtil: RedisUtil,
) : MailService {
    /**
     * ## 发送邮件
     * 发送邮件, 邮件模板由 [MailTemplateEnum] 枚举类定义, 模板参数由 [parameters] 指定; 邮件地址由 [email] 指定;
     * 根据模板和参数生成邮件内容, 并发送邮件
     *
     * @param email 邮件地址
     * @param template 邮件模板
     * @param parameters 模板参数
     */
    override fun sendMail(email: String, template: MailTemplateEnum, parameters: Map<String, String?>) {
        // 检查验证码的重新发送时间
        val getMail = Util.mapToObject(redisUtil.hashGet("mail:code:$email"), RedisMailCodeDO::class.java)
        if (getMail != null) {
            val getSendAfterTime = System.currentTimeMillis() - getMail.sendAt.toLong()
            if (getSendAfterTime < 60000L) {
                throw BusinessException(
                    "周期时间短信验证码发送频率过高，请等待 15 分钟（剩余 ${getSendAfterTime / 1000} 秒）",
                    ErrorCode.OVER_SPEED,
                )
            }
            if (getMail.frequency.toLong() >= 5) {
                throw BusinessException("验证码发送次数过多，请等待 15 分钟", ErrorCode.OVER_SPEED)
            } else {
                val sendMail = RedisMailCodeDO().also {
                    it.sendAt = System.currentTimeMillis().toString()
                    it.frequency = getMail.frequency + 1
                    it.code = parameters["code"]
                }
                redisUtil.hashSet("mail:code:$email", sendMail)
            }
        } else {
            if (parameters["code"] != null) {
                val sendMail = RedisMailCodeDO().also {
                    it.sendAt = System.currentTimeMillis().toString()
                    it.frequency = "1"
                    it.code = parameters["code"]
                }
                redisUtil.hashSet("mail:code:$email", sendMail)
            }
        }
        // 检查邮件模板是否存在
        val getFileInfo = ClassPathResource("templates/mail/${template.template}.html")
        if (!getFileInfo.exists()) {
            throw MailTemplateNotFoundException("邮件模板不存在")
        }
        // 模板自动注入参数
        val param = HashMap<String, Any?>(parameters)
        param["title"] = BaseDataConstant.SERVICE_TITLE
        param["subject"] = template.subject
        // 创建邮件发送内容
        val message: MimeMessage = javaMailSender.createMimeMessage()
        val context = Context().also { ctx -> ctx.setVariables(param) }
        val templateContext = templateEngine.process(getFileInfo.path.replace("templates/", ""), context)
        try {
            MimeMessageHelper(message, true).also { helper ->
                helper.setFrom(BaseDataConstant.mailUsername!!)
                helper.setTo(email)
                helper.setSubject(template.subject)
                helper.setText(templateContext, true)
            }
            javaMailSender.send(message)
            log.debug("[MAIL] 发送邮件 {} 标题 {} 成功", email, parameters["title"].toString())
        } catch (e: MessagingException) {
            throw MailSendException("发送邮件失败", e)
        }
    }

    /**
     * ## 发送验证码邮件
     * 发送验证码邮件, 邮件模板由 [MailTemplateEnum] 枚举类定义, 验证码由 [verifyCode] 指定; 邮件地址由 [email] 指定;
     * 根据模板和验证码生成邮件内容, 并发送邮件
     *
     * @param email 邮件地址
     * @param verifyCode 验证码
     * @param template 邮件模板
     */
    @Transactional
    override fun sendVerifyCodeMail(email: String, verifyCode: String, template: MailTemplateEnum) {
        // 检查该模板是否允许发送验证码
        if (!template.hashCode) {
            throw MailTemplateNotFoundException("该模板不支持发送验证码")
        }
        val parameters = mapOf("code" to verifyCode)
        this.sendMail(email, template, parameters)
    }
}
