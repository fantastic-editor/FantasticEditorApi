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

package com.frontleaves.fantasticeditor.controllers

import com.frontleaves.fantasticeditor.constant.BaseDataConstant
import com.frontleaves.fantasticeditor.models.dto.InfoStatusDTO
import com.frontleaves.fantasticeditor.models.dto.InfoWebDTO
import com.frontleaves.fantasticeditor.utility.BaseResponse
import com.frontleaves.fantasticeditor.utility.ResultUtil
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.management.ManagementFactory
import java.text.SimpleDateFormat

/**
 * 首页控制器
 *
 * 用于处理首页请求
 *
 * @since v1.0.0
 * @constructor 创建一个首页控制器
 * @author xiao_lfeng
 */
@RestController
class IndexController {
    /**
     * 状态查询
     *
     * 用于检查网站状态, 如系统信息等状态概要表, 用于检查网站是否正常运行
     *
     * @return 返回网站状态
     */
    @GetMapping("/")
    fun webStatus(): ResponseEntity<BaseResponse<InfoStatusDTO>> {
        val properties = System.getProperties()
        val iterator = properties.entries.iterator()
        val infoStatusDTO = InfoStatusDTO()
        while (iterator.hasNext()) {
            val systemParam = iterator.next()
            systemParam.key.takeIf { it == "user.country" }?.let { infoStatusDTO.userCountry = systemParam.value.toString() }
            systemParam.key.takeIf { it == "os.name" }?.let { infoStatusDTO.osName = systemParam.value.toString() }
            systemParam.key.takeIf { it == "user.timezone" }?.let { infoStatusDTO.timeZone = systemParam.value.toString() }
            systemParam.key.takeIf { it == "file.encoding" }?.let { infoStatusDTO.charset = systemParam.value.toString() }
            systemParam.key.takeIf { it == "http.proxyHost" }?.let { infoStatusDTO.proxyHost = systemParam.value.toString() }
        }
        infoStatusDTO.startTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(ManagementFactory.getRuntimeMXBean().startTime)
        return ResultUtil.success("当前状态正常", infoStatusDTO)
    }

    /**
     * 网站图标
     *
     * 用于获取网站图标
     *
     * @return 返回网站图标
     */
    @GetMapping("/favicon.ico")
    fun favicon(): ResponseEntity<Any>? {
        val getFavicon = ClassPathResource("static/images/favicon.png").file
        if (getFavicon.exists()) {
            val header = HttpHeaders().also {
                it.contentType = org.springframework.http.MediaType.IMAGE_PNG
            }
            return ResponseEntity.ok().headers(header).body(getFavicon.readBytes())
        } else {
            return null
        }
    }

    /**
     * 获取全局概要信息
     *
     * 用于获取信息, 用于获取前端一些需要展示的信息，如网站名称等。允许网站进行热修改操作
     *
     * @return 返回信息
     */
    @GetMapping("/info")
    fun getInformation(): ResponseEntity<BaseResponse<InfoWebDTO>> {
        val infoWeb = InfoWebDTO().also {
            it.title = BaseDataConstant.SERVICE_TITLE
            it.subTitle = "高效协作与轻松创作的利器"
            it.webDescription = BaseDataConstant.SERVICE_SUB_TITLE
            it.webIcon = "http://localhost:8080/favicon.ico"
        }
        return ResultUtil.success("获取信息成功", infoWeb)
    }
}
