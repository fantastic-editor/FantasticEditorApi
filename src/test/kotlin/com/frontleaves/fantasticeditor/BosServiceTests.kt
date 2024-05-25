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

package com.frontleaves.fantasticeditor

import com.frontleaves.fantasticeditor.annotations.KSlf4j.Companion.log
import com.frontleaves.fantasticeditor.dao.UserDAO
import com.frontleaves.fantasticeditor.services.interfaces.BosService
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * # 百度 Bukkit 服务测试
 * 用于测试百度 Bukkit 服务的相关功能
 *
 * @since v1.0.0
 * @property bosService 百度 Bukkit 服务
 * @constructor 创建一个 BCE 服务测试
 * @author xiao_lfeng
 */
@SpringBootTest
class BosServiceTests {

    @Resource
    private lateinit var bosService: BosService

    @Resource
    private lateinit var userDAO: UserDAO

    @Test
    fun testImageUpload() {
        val getResource = ClassPathResource("static/images/demo-upload.png")
        val inputStream = getResource.inputStream
        // 上传文件信息
        val uploadImage = bosService.uploadImage(inputStream)
        log.info("upload image: $uploadImage")
    }

    @OptIn(ExperimentalEncodingApi::class, ExperimentalStdlibApi::class)
    @Test
    fun testFileUpload() {
        //
        // 注意：这个方法是不合规的方法，文件的上传需要关联用户信息，这里仅仅是为了测试文件上传的功能
        //
        // 获取超级管理员信息
        val getUser = userDAO.getUserByUUID("b534e8a823253ec08cbb155d680240f2")
        // String(ClassPathResource("static/assets/demo-upload.doc").inputStream.readAllBytes(), StandardCharsets.UTF_8).let { log.info(it) }
        // log.debug(ClassPathResource("static/assets/demo-upload.doc").inputStream.readAllBytes().toHexString())
        // 上传文件信息
        if (getUser != null) {
            var uploadFile = bosService.uploadFile(ClassPathResource("static/assets/demo-upload.docx").inputStream, getUser)
            log.info("upload file: $uploadFile")
            uploadFile = bosService.uploadFile(ClassPathResource("static/assets/demo-upload.xlsx").inputStream, getUser)
            log.info("upload file: $uploadFile")
            uploadFile = bosService.uploadFile(ClassPathResource("static/assets/demo-upload.pptx").inputStream, getUser)
            log.info("upload file: $uploadFile")
            uploadFile = bosService.uploadFile(ClassPathResource("static/assets/demo-upload.ppt").inputStream, getUser)
            log.info("upload file: $uploadFile")
            uploadFile = bosService.uploadFile(ClassPathResource("static/assets/demo-upload.doc").inputStream, getUser)
            log.info("upload file: $uploadFile")
            uploadFile = bosService.uploadFile(ClassPathResource("static/assets/demo-upload.xls").inputStream, getUser)
            log.info("upload file: $uploadFile")
        }
    }
}
