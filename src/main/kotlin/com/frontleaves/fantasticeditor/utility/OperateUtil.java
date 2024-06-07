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

package com.frontleaves.fantasticeditor.utility;

import com.frontleaves.fantasticeditor.exceptions.BusinessException;
import com.frontleaves.fantasticeditor.models.entity.redis.RedisSmsCodeDO;
import com.frontleaves.fantasticeditor.utility.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 操作工具类
 * <p>
 * 用于处理一些操作相关的工具方法
 *
 * @author xiao_lfeng | xiangZr-hhh | DC_DC
 * @version v1.0.0
 * @see Util
 * @see ErrorCode
 * @since v1.0.0
 */
@Component
@RequiredArgsConstructor
public class OperateUtil {
    private final RedisUtil redisUtil;

    /**
     * 检查手机号是否可以发送短信
     * <p>
     * 检查手机号是否可以发送短信，如果可以发送短信则返回，否则抛出异常；
     * 重新发送的检查要求：
     * <li>两次发送短信的时间间隔不得小于 60 秒</li>
     * <li>15 分钟内最多发送5次短信</li>
     *
     * @param phone 手机号
     */
    public void checkSmsResendAble(final String phone) {
        RedisSmsCodeDO smsPhone = Util.INSTANCE.mapToObject(
                redisUtil.hashGet("sms:code:" + phone),
                RedisSmsCodeDO.class
        );
        if (smsPhone != null) {
            // 15分钟检查
            if (Long.parseLong(smsPhone.getFrequency()) >= 5) {
                long getTime = redisUtil.getExpire("sms:code:" + phone);
                throw new BusinessException(
                        "周期时间短信验证码发送频率过高，请等待 15 分钟（剩余 " + getTime + " 秒）",
                        ErrorCode.OVER_SPEED
                );
            }
            // 60 秒检查
            long getTime = Long.parseLong(smsPhone.getSendAt()) + 60 * 1000 - System.currentTimeMillis();
            if (getTime > 0) {
                throw new BusinessException("短信验证码发送频率太高（等待 " + getTime / 1000 + " 秒）", ErrorCode.OVER_SPEED);
            }
        }
    }



}
