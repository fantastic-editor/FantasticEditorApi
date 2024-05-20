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

package com.frontleaves.fantasticeditor.models.entity.redis;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Redis 邮件验证码数据对象
 * <hr/>
 * 用于定义 Redis 邮件验证码数据对象，用于定义 Redis 邮件验证码的基本信息；
 *
 * @since v1.0.0
 * @version v1.0.0
 * @author xiao_lfeng
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class RedisMailCodeDO {
    /**
     * 重发次数
     */
    public Long frequency;
    /**
     * 邮件验证码
     */
    public String code;
    /**
     * 发送时间
     */
    public Long sendAt;
}
