package yi.memberapi.application.impl.auth.command

import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.security.MemberUserDetailsService
import yi.memberapi.adapter.webapi.auth.dto.response.RefreshResponse
import yi.memberapi.application.impl.auth.service.AuthCookieManager
import yi.memberapi.application.provided.RedisTokenRepository
import yi.memberapi.application.required.TokenRefresher
import yi.memberapi.common.exception.AuthException
import yi.memberapi.common.util.JwtTokenProvider
import yi.memberapi.domain.token.RefreshTokenInfo
import java.time.Instant

@Service
@Transactional
class CommandTokenRefresh(
    private val userDetailsService: MemberUserDetailsService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTokenRepository: RedisTokenRepository,
    private val authCookieManager: AuthCookieManager
) : TokenRefresher {

    override fun refresh(refreshToken: String, clientIp: String, response: HttpServletResponse): RefreshResponse {
        val tokenInfo = redisTokenRepository.findRefreshToken(refreshToken)
            ?: throw AuthException.RefreshTokenNotFoundException()

        if (tokenInfo.isExpired()) {
            redisTokenRepository.deleteRefreshToken(refreshToken)
            authCookieManager.clearRefreshTokenCookie(response)
            throw AuthException.TokenExpiredException()
        }

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            redisTokenRepository.deleteRefreshToken(refreshToken)
            authCookieManager.clearRefreshTokenCookie(response)
            throw AuthException.InvalidTokenException()
        }

        if (tokenInfo.clientIp != clientIp) {
            redisTokenRepository.deleteAllUserTokens(tokenInfo.memberId)
            authCookieManager.clearRefreshTokenCookie(response)
            throw AuthException.IpMismatchException()
        }

        val userDetails = userDetailsService.loadUserByUsername(tokenInfo.username) as MemberUserDetails

        redisTokenRepository.deleteRefreshToken(refreshToken)

        val newAccessToken = jwtTokenProvider.generateAccessToken(userDetails, tokenInfo.memberId)
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(userDetails, tokenInfo.memberId, tokenInfo.rememberMe)

        val refreshTokenExpirationSeconds = jwtTokenProvider.getRefreshTokenExpirationSeconds(tokenInfo.rememberMe)
        val newTokenInfo = RefreshTokenInfo(
            memberId = tokenInfo.memberId,
            username = tokenInfo.username,
            tokenHash = authCookieManager.hashToken(newRefreshToken),
            clientIp = clientIp,
            rememberMe = tokenInfo.rememberMe,
            expiresAt = Instant.now().plusSeconds(refreshTokenExpirationSeconds).toEpochMilli()
        )
        redisTokenRepository.saveRefreshToken(newRefreshToken, newTokenInfo, refreshTokenExpirationSeconds)

        val accessTokenExpirationSeconds = jwtTokenProvider.getAccessTokenExpirationSeconds()
        redisTokenRepository.saveAccessToken(newAccessToken, tokenInfo.memberId, accessTokenExpirationSeconds)

        response.setHeader("Authorization", "Bearer $newAccessToken")

        val refreshCookie = authCookieManager.createRefreshTokenCookie(newRefreshToken, refreshTokenExpirationSeconds.toInt())
        response.addCookie(refreshCookie)

        return RefreshResponse(
            expiresIn = accessTokenExpirationSeconds
        )
    }
}
