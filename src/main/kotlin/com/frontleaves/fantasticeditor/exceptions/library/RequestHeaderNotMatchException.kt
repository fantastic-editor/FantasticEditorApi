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

package com.frontleaves.fantasticeditor.exceptions.library

/**
 * # 请求头未匹配异常
 * 用于定义请求头需要匹配对应参数异常、参数不存在、参数错误等；
 *
 * @since v1.0.0
 * @see RuntimeException
 * @property message 异常信息
 * @constructor 创建一个请求参数异常
 * @author xiao_lfeng
 */
class RequestHeaderNotMatchException(override val message: String) : RuntimeException(message)
