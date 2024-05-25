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

import com.frontleaves.fantasticeditor.models.dto.UserCurrentDTO
import com.frontleaves.fantasticeditor.models.vo.api.auth.AuthUserLoginVO
import com.frontleaves.fantasticeditor.models.vo.api.auth.AuthUserRegisterVO

/**
 * ## 用户服务接口
 * 用于定义用户服务接口
 *
 * @since v1.0.0
 * @constructor 创建一个用户服务接口
 * @author xiao_lfeng
 */
interface UserService {
    /**
     * ## 用户注册
     * 用于用户注册
     *
     * @param authUserRegisterVO 用户注册信息
     * @return UserCurrentDTO
     */
    fun userRegister(authUserRegisterVO: AuthUserRegisterVO): UserCurrentDTO

    /**
     * ## 用户注册短信验证码
     * 用于用户注册进行的短信验证码操作，发送短信验证码有效时间十五分钟，存储数据存储在 redis 缓存中。将会返回是否成功发送 SMS 的结果。
     *
     * @return 成功发送返回真，否则返回假
     */
    fun sendRegisterPhoneCode(phone: String)

    /**
     * ## 用户登录
     * 用于用户登录，将使用选择语句和正则表达式判断用户登录方式,并校验用户登录信息
     *
     * @param authUserLoginVO 用户登录信息
     * @return UserCurrentDTO
     */
    fun userLogin(authUserLoginVO: AuthUserLoginVO): Boolean

    /**
     * ## 检查邮箱验证码
     * 用于用户登录进行的邮箱验证码操作，发送邮箱验证码有效时间十五分钟，存储数据存储在 redis 缓存中；根据数据检查的结果，若验证码检查通过返回
     * true 否则返回 false，当验证成功时，对应的验证码将会删除，修改用户的状态为已验证；若用户的状态本身为已验证，则将会抛出错误不许操作接口；
     * 若抛出后，如果验证码匹配成功将会回滚状态。
     *
     * @return 成功发送返回真，否则返回假
     */
    fun checkMailVerify(email: String, verifyCode: String)
}
