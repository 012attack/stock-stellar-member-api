package yi.memberapi.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secret: String,
    val accessTokenExpiration: Long = 1800000,          // 30분
    val refreshTokenExpiration: Long = 86400000,        // 24시간
    val refreshTokenExpirationRememberMe: Long = 31536000000  // 1년 (remember me)
)
