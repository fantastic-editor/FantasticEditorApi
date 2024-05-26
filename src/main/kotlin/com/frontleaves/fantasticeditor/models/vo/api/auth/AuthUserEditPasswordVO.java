package com.frontleaves.fantasticeditor.models.vo.api.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户修改密码VO
 * <p>
 * 用于接收用户修改密码的信息
 *
 * @author DC_DC
 * @version v1.0.0
 * @since v1.0.0
 */
@Data
@NoArgsConstructor
public class AuthUserEditPasswordVO {
    @NotBlank(message = "原密码不能为空")
    public String oldPassword;
    @NotBlank(message = "新密码不能为空")
    public String newPassword;
}
