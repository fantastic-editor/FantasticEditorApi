package com.frontleaves.fantasticeditor.config.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

/**
 * # Redis 配置
 * 用于配置 Redis 的连接
 *
 * @since v1.0.0
 * @property env 环境变量
 * @constructor 创建一个 Redis 配置
 * @property host 主机
 * @property port 端口
 * @property password 密码
 *
 * @since v1.0.0
 * @author xiao_lfeng
 */
@Configuration
class RedisConfig(private val env: Environment) {
    private val host = env.getProperty("spring.data.redis.host")
    private val port = env.getProperty("spring.data.redis.port")
    private val password = env.getProperty("spring.data.redis.password")

    /**
     * ## Jedis 连接工厂
     * 用于定义 Jedis 连接工厂
     *
     * @return JedisConnectionFactory
     */
    @Bean
    fun jedisConnectionFactory(): JedisConnectionFactory {
        return JedisConnectionFactory(
            RedisStandaloneConfiguration().also {
                it.hostName = host ?: "localhost"
                it.port = port?.toInt() ?: 6379
                it.password = RedisPassword.of(password)
            },
        )
    }

    /**
     * ## Redis 模板
     * 用于定义 Redis 模板
     *
     * @param connectionFactory Redis 连接工厂
     * @return RedisTemplate<String, Any>
     */
    @Bean
    fun redisTemplate(connectionFactory: RedisConnectionFactory?): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        // 配置Redis编码格式
        val stringSerializer: RedisSerializer<String> = StringRedisSerializer()
        val jsonSerializer: RedisSerializer<Any> = GenericJackson2JsonRedisSerializer()
        // 开启事务支持
        redisTemplate.setEnableTransactionSupport(true)
        return redisTemplate.also {
            it.connectionFactory = connectionFactory
            it.keySerializer = stringSerializer
            it.valueSerializer = jsonSerializer
            it.hashKeySerializer = stringSerializer
            it.hashValueSerializer = jsonSerializer
        }
    }

    /**
     * ## 事务管理器
     * 用于定义事务管理器
     *
     * @param dataSource 数据源
     * @return PlatformTransactionManager
     */
    @Bean
    fun transactionManager(dataSource: DataSource): PlatformTransactionManager {
        return DataSourceTransactionManager(dataSource)
    }
}
