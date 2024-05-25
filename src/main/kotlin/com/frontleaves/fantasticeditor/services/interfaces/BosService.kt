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

package com.frontleaves.fantasticeditor.services.interfaces

import com.frontleaves.fantasticeditor.models.entity.sql.SqlUserDO
import java.io.InputStream

/**
 * # 百度 Bukkit 服务接口
 * 用于定义百度 Bukkit 服务接口, 包括一些文件的上传，文件的修改，文件的删除，以及一些自定义相关的部分配置信息
 *
 * @since v1.0.0
 * @constructor 创建一个 BCE 服务接口
 * @author xiao_lfeng
 */
interface BosService {
    /**
     * ## 上传图片
     * 上传图片到百度 BCE 服务，通过数据流的形式将数据传入到 BCE 服务，随后返回图片的命名信息；若出现错误将抛出异常返回；
     * 图片格式支持 jpg, png, gif, webp；
     * 注意：该方法不能够自动将图片信息直接存入数据库，仅是对图片的上传操作；
     *
     * @param stream 图片流
     * @throws RuntimeException 文件格式错误
     * @return 返回图片的命名信息
     */
    @Throws(RuntimeException::class)
    fun uploadImage(stream: InputStream): String

    /**
     * ## 上传文件
     * 上传文件到百度 BCE 服务，通过数据流的形式将数据传入到 BCE 服务，随后返回文件的命名信息；若出现错误将抛出异常返回;
     * 支持的文件格式有：txt, doc, docx, xls, xlsx, ppt, pptx；
     * 注意：该方法不能够自动将文件信息直接存入数据库，仅是对文件的上传操作；
     *
     * @param stream 文件流
     * @param user 用户实体类
     * @return 返回文件的命名信息
     */
    fun uploadFile(stream: InputStream, user: SqlUserDO): String

    /**
     * ## 删除文件
     * 删除百度 BCE 服务中的文件，通过文件的命名信息将文件删除；若文件不存在返回false，若存在且删除成功则返回 true;
     *
     * @param fileName 文件名
     * @param user 用户实体类
     * @return 返回删除是否成功
     */
    fun deleteFile(fileName: String, user: SqlUserDO): Boolean

    /**
     * ## 删除图片
     * 删除百度 BCE 服务中的图片，通过图片的命名信息将图片删除；若图片不存在返回false，若存在且删除成功则返回 true;
     *
     * @param fileName 图片名
     * @return 返回删除是否成功
     */
    fun deleteImage(fileName: String): Boolean

    /**
     * ## 修改文件
     * 修改百度 BCE 服务中的文件，通过文件的命名信息将文件修改；若出现错误将抛出异常返回
     *
     * @param fileName 文件名
     * @param stream 文件流
     */
    fun modifyFile(fileName: String, stream: InputStream): Boolean

    /**
     * ## 上传临时文件
     * 上传临时文件到百度 BCE 服务，通过数据流的形式将数据传入到 BCE 服务，随后返回文件的命名信息；若出现错误将抛出异常返回
     *
     * @param stream 文件流
     * @return 返回文件的命名信息
     */
    fun uploadTempFile(stream: InputStream): String
}
