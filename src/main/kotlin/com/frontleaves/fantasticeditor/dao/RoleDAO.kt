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
import com.frontleaves.fantasticeditor.mappers.RoleMapper
import com.frontleaves.fantasticeditor.models.entity.sql.SqlRoleDO
import com.frontleaves.fantasticeditor.utility.Util
import com.frontleaves.fantasticeditor.utility.redis.RedisUtil
import org.springframework.stereotype.Repository

/**
 * # 角色数据访问对象
 * 用于定义角色数据访问对象；
 *
 * @since v1.0.0
 * @see ServiceImpl
 * @see IService
 * @constructor 创建一个角色数据访问对象
 * @author xiao_lfeng
 */
@Repository
class RoleDAO(private val redisUtil: RedisUtil) : ServiceImpl<RoleMapper, SqlRoleDO>(), IService<SqlRoleDO> {

    /**
     * ## 通过UUID获取角色
     * 通过UUID获取角色的基本信息
     *
     * @param ruuid 角色UUID
     * @return 角色实体类
     */
    fun getRoleByRUUID(ruuid: String): SqlRoleDO? {
        return redisUtil.hashGet("role:uuid:$ruuid").takeIf { !it.isNullOrEmpty() }?.let {
            Util.mapToObject(it, SqlRoleDO::class.java)
        } ?: run {
            this.getOne(QueryWrapper<SqlRoleDO>().eq(SqlRoleDO::ruuid.name, ruuid))?.run {
                redisUtil.hashSet("role:uuid:$ruuid", Util.objectToMap(this), 3600 * 24)
                this
            }
        }
    }

    /**
     * ## 通过角色名获取角色
     * 通过角色名获取角色的基本信息
     *
     * @param roleName 角色名
     * @return 角色实体类
     */
    fun getRoleByRoleName(roleName: String): SqlRoleDO? {
        return redisUtil.hashGet("role:name:$roleName").takeIf { !it.isNullOrEmpty() }?.let {
            Util.mapToObject(it, SqlRoleDO::class.java)
        } ?: run {
            this.getOne(QueryWrapper<SqlRoleDO>().eq(SqlRoleDO::name.name, roleName))?.run {
                redisUtil.hashSet("role:name:$roleName", this, 3600 * 24)
                this
            }
        }
    }

    /**
     * ## 添加角色
     * 添加角色，根据传入角色信息对角色进行添加；
     * 添加成功后返回操作成功的结果，否则返回失败；
     *
     * @param role 角色实体类
     * @return 添加结果
     * */
    fun addRole(role: SqlRoleDO): Boolean {
        return this.save(role)
    }
}
