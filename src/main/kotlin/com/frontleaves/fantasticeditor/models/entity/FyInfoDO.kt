package com.frontleaves.fantasticeditor.models.entity

import com.baomidou.mybatisplus.annotation.TableName
import java.sql.Timestamp

/**
 * # 基本信息实体类
 * 用于定义基本信息实体类；
 *
 * @since v1.0.0
 * @property id 唯一标识
 * @property key 键
 * @property value 值
 * @property updatedAt 更新时间
 * @constructor 创建一个基本信息实体类
 * @author xiao_lfeng
 */
@TableName("fy_info")
data class FyInfoDO(
    val id: String,
    val key: String,
    val value: String,
    val updatedAt: Timestamp,
)
