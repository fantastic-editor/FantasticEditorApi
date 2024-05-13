package com.frontleaves.fantasticeditor.annotations

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * # Slf4j 注解
 * 用于在类上添加 Slf4j 日志
 *
 * @since v1.0.0
 * @see Logger
 * @author xiao_lfeng
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Slf4j {
    companion object {
        val <reified T> T.log: Logger
            inline get() = LoggerFactory.getLogger(T::class.java)
    }
}
