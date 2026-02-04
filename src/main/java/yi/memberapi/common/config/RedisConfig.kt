package yi.memberapi.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import yi.memberapi.domain.token.RefreshTokenInfo

@Configuration
class RedisConfig {

    @Bean
    fun redisObjectMapper(): ObjectMapper {
        return ObjectMapper().apply {
            registerModule(KotlinModule.Builder().build())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
    }

    @Bean
    fun refreshTokenRedisTemplate(
        connectionFactory: RedisConnectionFactory,
        redisObjectMapper: ObjectMapper
    ): RedisTemplate<String, RefreshTokenInfo> {
        val serializer = Jackson2JsonRedisSerializer(redisObjectMapper, RefreshTokenInfo::class.java)
        return RedisTemplate<String, RefreshTokenInfo>().apply {
            setConnectionFactory(connectionFactory)
            keySerializer = StringRedisSerializer()
            valueSerializer = serializer
            hashKeySerializer = StringRedisSerializer()
            hashValueSerializer = serializer
        }
    }

    @Bean
    fun tokenStringRedisTemplate(
        connectionFactory: RedisConnectionFactory
    ): RedisTemplate<String, String> {
        return RedisTemplate<String, String>().apply {
            setConnectionFactory(connectionFactory)
            keySerializer = StringRedisSerializer()
            valueSerializer = StringRedisSerializer()
            hashKeySerializer = StringRedisSerializer()
            hashValueSerializer = StringRedisSerializer()
        }
    }
}
