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

import org.jetbrains.annotations.NotNull;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * 处理工具类
 * <p>
 * 用于处理对象的转换、处理等
 *
 * @since v1.0.0
 * @version v1.0.0
 * @author xiao_lfeng
 */
public class ProcessingUtil {

    /**
     * 生成密码
     * <p>
     * 生成密码，先将密码转换为base64编码，再使用BCrypt加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    @NotNull
    public static String generatePassword(String password) {
        String base64Password = BasicUtil.INSTANCE.makeStringToBase64(password);
        return BCrypt.hashpw(base64Password, BCrypt.gensalt());
    }

    /**
     * 验证密码
     * <p>
     * 验证密码，先将密码转换为base64编码，再使用BCrypt验证
     *
     * @param password 密码
     * @param hashedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean verifyPassword(String password, String hashedPassword) {
        String base64Password = BasicUtil.INSTANCE.makeStringToBase64(password);
        return BCrypt.checkpw(base64Password, hashedPassword);
    }
}
