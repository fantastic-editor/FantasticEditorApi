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

package com.frontleaves.fantasticeditor.services

import com.baidubce.services.bos.BosClient
import com.frontleaves.fantasticeditor.annotations.KSlf4j.Companion.log
import com.frontleaves.fantasticeditor.constant.BceDataConstant
import com.frontleaves.fantasticeditor.models.entity.sql.SqlUserDO
import com.frontleaves.fantasticeditor.services.interfaces.BceService
import com.frontleaves.fantasticeditor.utility.Util
import org.springframework.stereotype.Service
import java.io.InputStream
import java.util.*

@Service
@OptIn(ExperimentalStdlibApi::class)
class BceServiceImpl(
    private val bosClient: BosClient,
) : BceService {
    /**
     * ## 上传图片
     * 上传图片到百度 BCE 服务，通过数据流的形式将数据传入到 BCE 服务，随后返回图片的命名信息；若出现错误将抛出异常返回
     *
     * @param stream 图片流
     * @throws RuntimeException 文件格式错误
     * @return 返回图片的命名信息
     */
    @Throws(RuntimeException::class)
    override fun uploadImage(stream: InputStream): String {
        // 获取文件的格式
        val byteRead = stream.readAllBytes()
        if (byteRead.size < 12) {
            throw RuntimeException("文件格式错误")
        }

        val fileTypeHex = bytesToHex(byteRead).uppercase(Locale.getDefault())
        val fileType = if (fileTypeHex.startsWith("FFD8FF")) {
            "jpg"
        } else if (fileTypeHex.startsWith("89504E47")) {
            "png"
        } else if (fileTypeHex.startsWith("47494638")) {
            "gif"
        } else {
            (fileTypeHex.startsWith("52494646") && fileTypeHex.substring(8, 16) == "57454250")
                .takeIf { it }
                ?.let { "webp" } ?: throw RuntimeException("文件格式错误 ($fileTypeHex)")
        }

        // 文件上传
        val getFileName = StringBuilder(Util.makeNoDashUUID())
            .append(".")
            .append(fileType)
            .toString()
        val getObjectKey = StringBuilder("avatars/")
            .append(getFileName)
            .toString()
        val putToBos = bosClient.putObject(BceDataConstant.bosBucketName, getObjectKey, byteRead)
        log.debug(putToBos.eTag)
        return getFileName
    }

    /**
     * ## 上传文件
     * 上传文件到百度 BCE 服务，通过数据流的形式将数据传入到 BCE 服务，随后返回文件的命名信息；若出现错误将抛出异常返回
     *
     * @param stream 文件流
     * @return 返回文件的命名信息
     */
    override fun uploadFile(stream: InputStream, user: SqlUserDO): String {
        TODO("Not yet implemented")
    }

    /**
     * ## 删除文件
     * 删除百度 BCE 服务中的文件，通过文件的命名信息将文件删除；若出现错误将抛出异常返回
     *
     * @param fileName 文件名
     */
    override fun deleteFile(fileName: String): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * ## 删除图片
     * 删除百度 BCE 服务中的图片，通过图片的命名信息将图片删除；若出现错误将抛出异常返回
     */
    override fun deleteImage(fileName: String): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * ## 修改文件
     * 修改百度 BCE 服务中的文件，通过文件的命名信息将文件修改；若出现错误将抛出异常返回
     *
     * @param fileName 文件名
     * @param stream 文件流
     */
    override fun modifyFile(fileName: String, stream: InputStream): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * ## 字节转换为十六进制
     * 将字节转换为十六进制, 用于文件格式的判断, 以及文件的命名
     *
     * @param bytes 字节
     * @return 返回十六进制字符串
     */
    private fun bytesToHex(bytes: ByteArray): String {
        val sb = java.lang.StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02X", b))
        }
        return sb.toString()
    }
}
