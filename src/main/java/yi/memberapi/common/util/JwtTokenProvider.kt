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

    fun generateAccessToken(userDetails: UserDetails): String {
        return generateToken(userDetails, jwtProperties.accessTokenExpiration)
    }

    fun generateRefreshToken(userDetails: UserDetails): String {
        return generateToken(userDetails, jwtProperties.refreshTokenExpiration)
    }

    private fun generateToken(userDetails: UserDetails, expiration: Long): String {
        val now = Date()
        val expiryDate = Date(now.time + expiration)

        return Jwts.builder()
            .subject(userDetails.username)
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

    fun getExpirationFromToken(token: String): Date {
        val claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload

        return claims.expiration
    }

    fun getAccessTokenExpiration(): Long = jwtProperties.accessTokenExpiration

    fun getRefreshTokenExpiration(): Long = jwtProperties.refreshTokenExpiration
}
