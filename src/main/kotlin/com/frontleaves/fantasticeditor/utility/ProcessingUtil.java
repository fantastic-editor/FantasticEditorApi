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
