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

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 网站信息数据传输对象
 * <hr/>
 * 网站信息数据传输对象，用于返回网站信息的数据；
 *
 * @since v1.0.0
 * @version v1.0.0
 * @author xiao_lfeng
 */
@Data
@NoArgsConstructor
public class InfoWebDTO {
    /**
     * 网站标题
     */
    public String title;
    /**
     * 网站副标题
     */
    public String subTitle;
    /**
     * 网站图标
     */
    public String webIcon;
    /**
     * 网站描述
     */
    public String webDescription;
    /**
     * 网站关键字
     */
    public String webKeywords;
    /**
     * 网站版权
     */
    public String webCopyRight;
}
