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

import com.frontleaves.fantasticeditor.dao.UserDAO
import jakarta.annotation.Resource
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

/**
 * ## 系统测试
 * 用于定义系统的测试
 *
 * @since v1.0.0
 * @property userDAO 用户数据访问对象
 * @constructor 创建一个系统测试
 * @author xiao_lfeng
 */
@SpringBootTest
class FantasticEditorApplicationTests {

    @Resource
    private lateinit var userDAO: UserDAO

    @Test
    fun testUserDAO() {
        println(UUID.nameUUIDFromBytes("xiao_lfeng".toByteArray()).toString())
        userDAO.getUserByUUID("uuid")
    }
}
