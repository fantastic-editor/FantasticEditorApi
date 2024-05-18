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

package com.frontleaves.fantasticeditor.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 信息状态数据传输对象
 * <p>
 * 用于传输信息状态数据, 例如代理主机, 代理端口等;
 * 在 {@code InfoController} 中使用;
 *
 * @since v1.0.0
 * @author xiao_lfeng
 */
@Data
@NoArgsConstructor
public class InfoStatusDTO {
    @NotBlank
    public String proxyHost;

    @NotBlank
    public String charset;

    @NotBlank
    public String timeZone;

    @NotBlank
    public String osName;

    @NotBlank
    public String userCountry;

    @NotBlank
    public String startTime;
}
