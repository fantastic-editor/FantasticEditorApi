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
import com.frontleaves.fantasticeditor.constant.BceDataConstant
import com.frontleaves.fantasticeditor.models.entity.sql.SqlUserDO
import com.frontleaves.fantasticeditor.services.interfaces.BosService
import com.frontleaves.fantasticeditor.utility.Util
import org.springframework.stereotype.Service
import java.io.InputStream
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

/**
 * # 百度 Bukkit 服务实现
 * 用于实现百度 Bukkit 服务接口, 包括一些文件的上传，文件的修改，文件的删除，以及一些自定义相关的部分配置信息
 *
 * @since v1.0.0
 * @constructor 创建一个 BCE 服务实现
 * @author xiao_lfeng
 */
@Service
class BosServiceImpl(
    private val bosClient: BosClient,
) : BosService {

    @Throws(RuntimeException::class)
    override fun uploadImage(stream: InputStream): String {
        // 获取文件的格式
        val byteRead = stream.readAllBytes()
        if (byteRead.size < 12) {
            throw RuntimeException("文件格式错误")
        }
        val fileType = bytesToHex(byteRead).uppercase(Locale.getDefault()).let {
            it.takeIf { it.startsWith("FFD8FF") }?.let { "jpeg" }
                ?: it.takeIf { it.startsWith("89504E47") }?.let { "png" }
                ?: it.takeIf { it.startsWith("47494638") }?.let { "gif" }
                ?: it.takeIf { it.startsWith("52494646") && it.substring(8, 16) == "57454250" }?.let { "webp" }
                ?: throw RuntimeException("文件格式错误 ($it)")
        }
        // 文件上传
        val getFileName = StringBuilder(Util.makeNoDashUUID())
            .append(".")
            .append(fileType)
            .toString()
        bosClient.putObject(BceDataConstant.bosBucketName, "avatars/$getFileName", byteRead)
        return getFileName
    }

    @Throws(RuntimeException::class)
    override fun uploadFile(stream: InputStream, user: SqlUserDO): String {
        // 获取文件的格式
        val byteRead = stream.readAllBytes()
        if (byteRead.size < 12) {
            throw RuntimeException("文件格式错误")
        }
        val fileType = bytesToHex(byteRead).uppercase(Locale.getDefault()).run {
            this.takeIf { it.startsWith("D0CF11E0") }?.let { oldOffice ->
                takeIf { oldOffice.contains("576F7264") }?.let { "doc" }
                    ?: takeIf { oldOffice.contains("506F776572506F696E74") }?.let { "ppt" }
                    ?: takeIf { oldOffice.contains("57006F0072006B0062006F006F006B") }?.let { "xls" }
                    ?: throw RuntimeException("文件格式错误 ($oldOffice|oldOffice)")
            } ?: this.takeIf { it.startsWith("504B0304140006") }?.let { newOffice ->
                try {
                    val zipInputStream = ZipInputStream(byteRead.inputStream())
                    var newZipEntry: ZipEntry? = zipInputStream.nextEntry
                    while (newZipEntry != null) {
                        if (newZipEntry.name.startsWith("word/")) {
                            return@let "docx"
                        } else if (newZipEntry.name.startsWith("ppt/")) {
                            return@let "pptx"
                        } else if (newZipEntry.name.startsWith("xl/")) {
                            return@let "xlsx"
                        }
                        newZipEntry = zipInputStream.nextEntry
                    }
                    zipInputStream.close()
                    throw RuntimeException("文件格式错误 ($newOffice|newOffice)")
                } catch (e: Exception) {
                    throw RuntimeException("文件格式错误 ($newOffice|newOffice)")
                }
            } ?: this.takeIf {
                it.startsWith("EFBBBF") || it.startsWith("FFFE") || it.startsWith("FEFF") || it.matches(
                    Regex("^[\\x20-\\x7E]+$"),
                )
            }?.let { "md" } ?: throw RuntimeException("文件格式错误 ($this)")
        }
        // 文件上传
        val getFileName = StringBuilder(Util.makeNoDashUUID())
            .append(".")
            .append(fileType)
            .toString()
        bosClient.putObject(BceDataConstant.bosBucketName, "files/${user.uuid}/$getFileName", byteRead)
        return getFileName
    }

    override fun deleteFile(fileName: String, user: SqlUserDO): Boolean {
        // 检查文件是否存在
        bosClient.doesObjectExist(BceDataConstant.bosBucketName, "files/${user.uuid}/$fileName")
            .takeIf { it }?.let {
                bosClient.deleteObject(BceDataConstant.bosBucketName, "files/${user.uuid}/$fileName")
            } ?: return false
        return true
    }

    override fun deleteImage(fileName: String): Boolean {
        // 检查文件是否存在
        bosClient.doesObjectExist(BceDataConstant.bosBucketName, "avatars/$fileName")
            .takeIf { it }?.let {
                bosClient.deleteObject(BceDataConstant.bosBucketName, "avatars/$fileName")
            } ?: return false
        return true
    }

    override fun modifyFile(fileName: String, stream: InputStream): Boolean {
        TODO("Not yet implemented")
    }

    override fun uploadTempFile(stream: InputStream): String {
        TODO("Not yet implemented")
    }

    /**
     * ## 字节转十六进制
     * 将字节数组转换为十六进制字符串, 用于文件格式的判断, 以及文件的上传;
     *
     * @param bytes 字节数组
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
