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
import com.frontleaves.fantasticeditor.models.entity.FyUserDO
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
 * @property FyUserDO 用户实体类
 * @constructor 创建一个用户数据访问对象
 * @author xiao_lfeng
 */
@Repository
class UserDAO(
    private val redisUtil: RedisUtil,
) : ServiceImpl<UserMapper, FyUserDO>(), IService<FyUserDO> {

    /**
     * ## 通过UUID获取用户
     * 通过UUID获取用户的基本信息
     *
     * @param uuid 用户UUID
     * @return 用户实体类
     */
    fun getUserByUUID(uuid: String): FyUserDO? {
        return redisUtil.hashGet("user:uuid:$uuid").takeIf { !it.isNullOrEmpty() }?.let {
            Util.mapToObject(it, FyUserDO::class.java)
        } ?: run {
            this.getOne(QueryWrapper<FyUserDO>().eq("uuid", uuid))
        }
    }
}