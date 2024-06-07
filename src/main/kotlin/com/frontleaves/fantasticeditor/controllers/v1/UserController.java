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

package com.frontleaves.fantasticeditor.controllers.v1;

import com.frontleaves.fantasticeditor.exceptions.BusinessException;
import com.frontleaves.fantasticeditor.models.vo.api.user.UserMailVerifyVO;
import com.frontleaves.fantasticeditor.models.vo.api.user.UserPublicInfoVO;
import com.frontleaves.fantasticeditor.services.interfaces.UserService;
import com.frontleaves.fantasticeditor.utility.BaseResponse;
import com.frontleaves.fantasticeditor.utility.ErrorCode;
import com.frontleaves.fantasticeditor.utility.ResultUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 * <hr>
 * 用户控制器，用于处理用户相关的请求;
 *
 * @author xiao_lfeng
 * @version v1.0.0
 * @since v1.0.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
	private final UserService userService;

	/**
	 * 邮箱验证
	 * <hr>
	 * 用户邮箱验证的地址，用于验证用户的邮箱是否合法；邮箱验证码的发送会根据用户在注册后，执行 authResendMailVerify 方法时的邮箱地址发送；
	 * 验证码的有效时间为 15 分钟；获取到的验证码在当前接口进行验证；
	 *
	 * @return 注册结果
	 */
	@PostMapping("/mail/verify")
	public ResponseEntity<BaseResponse<Void>> userMailVerify(
			@RequestBody @Validated final UserMailVerifyVO userMailVerifyVO
	) {
		userService.checkMailVerify(userMailVerifyVO.getEmail(), userMailVerifyVO.getVerifyCode());
		return ResultUtil.success("验证成功");
	}

	/**
	 * 重发邮件验证码
	 * <hr>
	 * 用于用户在注册时，未收到验证码或者验证码失效时，重新发送验证码；
	 *
	 * @param email 邮箱地址
	 * @return 重发结果
	 */
	@GetMapping("/mail/resend")
	public ResponseEntity<BaseResponse<Void>> authResendMailVerify(
			@RequestParam("email") final String email
	) {
		if (email.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
			userService.sendMailVerify(email);
			return ResultUtil.success("发送成功");
		} else {
			throw new BusinessException("邮箱格式错误", ErrorCode.REQUEST_BODY_PARAMETERS_ERROR);
		}
	}

	/**
	 * 通过uuid获取用户信息
	 * <hr>
	 * 根据uuid参数查询用户的基本信息，信息包括：角色组信息，权限信息，付费会员信息，以及个人其他信息
	 *
	 * @param uuid 用户uuid
	 * @return 用户公开信息
	 */
	@GetMapping("/info/get/ByUUID/{uuid}")
	public ResponseEntity<BaseResponse<UserPublicInfoVO>> getMyUserProfileInfo(
			@PathVariable("uuid") final String uuid
	) {

		if (uuid == null || uuid.isEmpty()) {
			throw new BusinessException("uuid参数为空", ErrorCode.REQUEST_PARAMETERS_ERROR);
		}

		UserPublicInfoVO userVO = userService.getMyUserProfileInfo(uuid);
		return ResultUtil.success("获取当前用户信息成功", userVO);
	}

}
