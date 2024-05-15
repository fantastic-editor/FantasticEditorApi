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

import com.frontleaves.fantasticeditor.annotations.KSlf4j.Companion.log
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.stereotype.Component

/**
 * ## 操作日志中间件
 * 用于记录操作日志
 *
 * @since v1.0.0
 * @constructor 创建一个操作日志中间件
 * @author xiao_lfeng
 */
@Aspect
@Component
class OperationLogMiddle {
    /**
     * ## 控制器日志
     * 用于记录控制器的操作日志
     */
    @Before(
        "execution(* com.frontleaves.fantasticeditor.controllers.*.*(..)) " +
            "|| execution(* com.frontleaves.fantasticeditor.controllers.*.*.*(..))",
    )
    fun controllerLog(joinPoint: JoinPoint) {
        log.info("[CONTROL] 执行方法 {}:{}", joinPoint.signature.declaringType.simpleName, joinPoint.signature.name)
    }

    /**
     * ## 服务日志
     * 用于记录服务的操作日志
     */
    @Before("execution(* com.frontleaves.fantasticeditor.services.*.*(..))")
    fun serviceLog(joinPoint: JoinPoint) {
        log.info("[SERVICE] 执行方法 {}:{}", joinPoint.signature.declaringType.simpleName, joinPoint.signature.name)
    }

    /**
     * ## 数据访问对象日志
     * 用于记录数据访问对象的操作日志
     */
    @Before("execution(* com.frontleaves.fantasticeditor.dao.*.*(..))")
    fun daoLog(joinPoint: JoinPoint) {
        log.info("[DAO] 执行方法 {}:{}", joinPoint.signature.declaringType.simpleName, joinPoint.signature.name)
    }
}
