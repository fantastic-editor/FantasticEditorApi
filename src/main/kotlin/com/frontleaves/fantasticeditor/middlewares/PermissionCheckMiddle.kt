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

package com.frontleaves.fantasticeditor.middlewares

import com.frontleaves.fantasticeditor.annotations.CheckPermission
import com.frontleaves.fantasticeditor.dao.RoleDAO
import com.frontleaves.fantasticeditor.dao.UserDAO
import com.frontleaves.fantasticeditor.exceptions.UserAuthenticationException
import com.frontleaves.fantasticeditor.utility.Util
import com.frontleaves.fantasticeditor.utility.redis.RedisUtil
import com.google.gson.Gson
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * ## 权限检查中间件
 * 用于检查用户是否有权限访问某个接口
 *
 * @since v1.0.0
 * @constructor 创建一个权限检查中间件
 * @author xiao_lfeng
 */
@Aspect
@Component
class PermissionCheckMiddle(
    private val userDAO: UserDAO,
    private val roleDAO: RoleDAO,
    private val redisUtil: RedisUtil,
    private val gson: Gson,
) {

    /**
     * 检查权限
     *
     * 用于检查用户是否有权限访问某个接口
     *
     * @param joinPoint 切入点
     */
    @Before("@annotation(com.frontleaves.fantasticeditor.annotations.CheckPermission)")
    fun checkPermission(joinPoint: JoinPoint) {
        val servletRequest = RequestContextHolder.getRequestAttributes() as ServletRequestAttributes
        val request = servletRequest.request
        // 获取用户信息
        val getUserAuthorization = Util.getAuthorizationToken(request)
        val getUserUUID = Util.getHeaderUUID(request)

        // 获取方法签名
        val signature = joinPoint.signature as MethodSignature
        val checkPermission = signature.method.getAnnotation(CheckPermission::class.java)
        val getNeedPermission = checkPermission.value

        if (getUserAuthorization.isNullOrBlank() || getUserUUID.isNullOrBlank()) {
            throw UserAuthenticationException(UserAuthenticationException.ErrorType.USER_NOT_LOGIN, request)
        }
        // 从缓存找授权
        redisUtil.hashGet("auth:token:$getUserAuthorization").takeIf { !it.isNullOrEmpty() }?.let {
            // 获取用户所属角色
            userDAO.getUserByUUID(getUserUUID).takeIf { it != null }?.let { user ->
                roleDAO.getRoleByRUUID(user.role).takeIf { it != null }?.let { role ->
                    val getPermissionList = gson.fromJson<String>(role.permissions, List::class.java)
                    if (getPermissionList.contains(getNeedPermission)) {
                        return
                    } else {
                        throw UserAuthenticationException(UserAuthenticationException.ErrorType.PERMISSION_DENIED, request)
                    }
                } ?: throw UserAuthenticationException(UserAuthenticationException.ErrorType.USER_NOT_EXIST, request)
            } ?: throw UserAuthenticationException(UserAuthenticationException.ErrorType.TOKEN_EXPIRED, request)
        }
    }
}
