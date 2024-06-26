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

package com.frontleaves.fantasticeditor.constant

/**
 * # 基础数据常量
 * 用于存放一些基础数据常量，例如服务标题、副标题等
 *
 * @since v1.0.0
 * @author xiao_lfeng
 */
data object BaseDataConstant {
    const val SERVICE_TITLE = "妙笔智编"
    const val SERVICE_SUB_TITLE = "一个结合大小AI模型技术，通过百度飞桨AI Studio和文心ERNIE SDK开发的在线文档编辑器，旨在提高用户处理多模态信息的效率，实现智能润色、多媒体信息提取和智能格式排版等功能，以优化学习和工作体验。"

    var mailHost: String = "smtp.qiye.aliyun.com"
    var mailPort: Int = 25
    var mailUsername: String? = null
    var mailPassword: String? = null
    var mailEncoding: String = "UTF-8"
}
