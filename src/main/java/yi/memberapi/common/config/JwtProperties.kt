package yi.memberapi.common.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secret: String,
    val accessTokenExpiration: Long = 1800000,
    val refreshTokenExpiration: Long = 604800000,
    val refreshTokenExpirationRememberMe: Long = 2592000000
)
