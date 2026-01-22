package yi.memberapi.common.util

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import yi.memberapi.common.config.JwtProperties
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    private val jwtProperties: JwtProperties
) {
    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())
    }

    fun generateAccessToken(userDetails: UserDetails, memberId: Long): String {
        return generateToken(userDetails.username, memberId, jwtProperties.accessTokenExpiration, "access")
    }

    fun generateRefreshToken(userDetails: UserDetails, memberId: Long, rememberMe: Boolean): String {
        val expiration = if (rememberMe) {
            jwtProperties.refreshTokenExpirationRememberMe
        } else {
            jwtProperties.refreshTokenExpiration
        }
        return generateToken(userDetails.username, memberId, expiration, "refresh")
    }

    private fun generateToken(username: String, memberId: Long, expiration: Long, tokenType: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)

        return Jwts.builder()
            .subject(username)
            .claim("memberId", memberId)
            .claim("type", tokenType)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(secretKey)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
            true
        } catch (e: ExpiredJwtException) {
            false
        } catch (e: Exception) {
            false
        }
    }

    fun getUsernameFromToken(token: String): String {
        val claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

        return claims.subject
    }

    fun getMemberIdFromToken(token: String): Long {
        val claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

        return (claims["memberId"] as Number).toLong()
    }

    fun getExpirationFromToken(token: String): Date {
        val claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

        return claims.expiration
    }

    fun getAccessTokenExpiration(): Long = jwtProperties.accessTokenExpiration

    fun getRefreshTokenExpiration(rememberMe: Boolean): Long {
        return if (rememberMe) {
            jwtProperties.refreshTokenExpirationRememberMe
        } else {
            jwtProperties.refreshTokenExpiration
        }
    }

    fun getAccessTokenExpirationSeconds(): Long = jwtProperties.accessTokenExpiration / 1000

    fun getRefreshTokenExpirationSeconds(rememberMe: Boolean): Long = getRefreshTokenExpiration(rememberMe) / 1000
}
