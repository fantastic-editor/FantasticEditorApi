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

package com.frontleaves.fantasticeditor.dao

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.frontleaves.fantasticeditor.mappers.UserMapper
import com.frontleaves.fantasticeditor.models.entity.sql.SqlUserDO
import com.frontleaves.fantasticeditor.utility.Util
import com.frontleaves.fantasticeditor.utility.redis.RedisUtil
import org.springframework.stereotype.Repository

/**
 * # 用户数据访问对象
 * 用于定义用户数据访问对象；
 *
 * @since v1.0.0
 * @see ServiceImpl
 * @property UserMapper 用户映射器
 * @property SqlUserDO 用户实体类
 * @constructor 创建一个用户数据访问对象
 * @author xiao_lfeng
 */
@Repository
class UserDAO(
    private val redisUtil: RedisUtil,
) : ServiceImpl<UserMapper, SqlUserDO>(), IService<SqlUserDO> {

    /**
     * ## 通过UUID获取用户
     * 通过UUID获取用户的基本信息
     *
     * @param uuid 用户UUID
     * @return 用户实体类
     */
    fun getUserByUUID(uuid: String): SqlUserDO? {
        return redisUtil.hashGet("user:uuid:$uuid").takeIf { !it.isNullOrEmpty() }?.let {
            Util.mapToObject(it, SqlUserDO::class.java)
        } ?: run {
            this.getOne(QueryWrapper<SqlUserDO>().eq("uuid", uuid))
        }
    }

    /**
     * ## 通过用户名获取用户
     * 通过用户名获取用户的基本信息
     *
     * @param username 用户名
     * @return 用户实体类
     */
    fun getUserByUsername(username: String): SqlUserDO? {
        return redisUtil.hashGet("user:username:$username").takeIf { !it.isNullOrEmpty() }?.let {
            Util.mapToObject(it, SqlUserDO::class.java)
        } ?: run {
            this.getOne(QueryWrapper<SqlUserDO>().eq("username", username))
        }
    }

    /**
     * ## 通过邮箱获取用户
     * 通过邮箱获取用户的基本信息
     *
     * @param email 邮箱
     * @return 用户实体类
     */
    fun getUserByEmail(email: String): SqlUserDO? {
        return redisUtil.hashGet("user:email:$email").takeIf { !it.isNullOrEmpty() }?.let {
            Util.mapToObject(it, SqlUserDO::class.java)
        } ?: run {
            this.getOne(QueryWrapper<SqlUserDO>().eq("email", email))
        }
    }

    /**
     * ## 通过手机号获取用户
     * 通过手机号获取用户的基本信息
     *
     * @param phone 手机号
     * @return 用户实体类
     */
    fun getUserByPhone(phone: String): SqlUserDO? {
        return redisUtil.hashGet("user:phone:$phone").takeIf { !it.isNullOrEmpty() }?.let {
            Util.mapToObject(it, SqlUserDO::class.java)
        } ?: run {
            this.getOne(QueryWrapper<SqlUserDO>().eq("phone", phone))
        }
    }
}
