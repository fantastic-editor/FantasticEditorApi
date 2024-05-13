package com.frontleaves.fantasticeditor.config.configuration

import com.baomidou.mybatisplus.annotation.DbType
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor
import com.frontleaves.fantasticeditor.annotations.Slf4j.Companion.log
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * # MyBatisPlus 配置
 * 用于配置 MyBatisPlus 的配置
 *
 * @since v1.0.0
 * @constructor 创建一个 MyBatisPlus 配置
 * @author xiao_lfeng
 */
@Configuration
class MybatisPlusConfig {
    /**
     * MyBatisPlus分页插件
     * <hr></hr>
     * 用于配置MyBatisPlus的分页插件, 用于分页查询; 该插件会自动拦截分页查询的请求, 并进行分页查询
     *
     * @return 分页
     */
    @Bean
    fun mybatisPlusInterceptor(): MybatisPlusInterceptor {
        log.debug("[CONFIG] MyBatisPlus 分页配置初始化...")
        return MybatisPlusInterceptor().also {
            it.addInnerInterceptor(
                PaginationInnerInterceptor().also { interceptor ->
                    interceptor.maxLimit = 20L
                    interceptor.dbType = DbType.POSTGRE_SQL
                },
            )
        }
    }
}
