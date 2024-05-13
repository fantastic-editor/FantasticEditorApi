package com.frontleaves.fantasticeditor.dao

import com.baomidou.mybatisplus.extension.service.IService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.frontleaves.fantasticeditor.mappers.InfoMapper
import com.frontleaves.fantasticeditor.models.entity.FyInfoDO

/**
 * # 基本信息数据访问对象
 * 用于定义基本信息数据访问对象；
 *
 * @since v1.0.0
 * @see ServiceImpl
 * @property InfoMapper 基本信息映射器
 * @property FyInfoDO 基本信息实体类
 * @constructor 创建一个基本信息数据访问对象
 * @author xiao_lfeng
 */
class InfoDAO : ServiceImpl<InfoMapper, FyInfoDO>(), IService<FyInfoDO>
