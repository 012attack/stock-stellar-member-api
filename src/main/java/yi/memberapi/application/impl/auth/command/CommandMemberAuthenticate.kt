package yi.memberapi.application.impl.auth.command

import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.security.MemberUserDetailsService
import yi.memberapi.adapter.webapi.dto.request.LoginRequest
import yi.memberapi.adapter.webapi.dto.response.LoginResponse
import yi.memberapi.application.impl.auth.service.AuthCookieManager
import yi.memberapi.application.required.MemberAuthenticator
import yi.memberapi.application.provided.RedisTokenRepository
import yi.memberapi.common.exception.AuthException
import yi.memberapi.common.util.JwtTokenProvider
import yi.memberapi.domain.token.RefreshTokenInfo
import java.time.Instant

@Service
@Transactional
class CommandMemberAuthenticate(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: MemberUserDetailsService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTokenRepository: RedisTokenRepository,
    private val authCookieManager: AuthCookieManager
) : MemberAuthenticator {

    override fun login(request: LoginRequest, clientIp: String, response: HttpServletResponse): LoginResponse {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(request.username, request.password)
            )
        } catch (e: AuthenticationException) {
            throw AuthException.InvalidCredentialsException()
        }

        val userDetails = userDetailsService.loadUserByUsername(request.username) as MemberUserDetails
        val member = userDetails.getMember()
        val memberId = member.id!!

        redisTokenRepository.deleteAllUserTokens(memberId)

        val accessToken = jwtTokenProvider.generateAccessToken(userDetails, memberId)
        val refreshToken = jwtTokenProvider.generateRefreshToken(userDetails, memberId, request.rememberMe)

        val refreshTokenExpirationSeconds = jwtTokenProvider.getRefreshTokenExpirationSeconds(request.rememberMe)
        val refreshTokenInfo = RefreshTokenInfo(
            memberId = memberId,
            username = request.username,
            tokenHash = authCookieManager.hashToken(refreshToken),
            clientIp = clientIp,
            rememberMe = request.rememberMe,
            expiresAt = Instant.now().plusSeconds(refreshTokenExpirationSeconds).toEpochMilli()
        )
        redisTokenRepository.saveRefreshToken(refreshToken, refreshTokenInfo, refreshTokenExpirationSeconds)

        val accessTokenExpirationSeconds = jwtTokenProvider.getAccessTokenExpirationSeconds()
        redisTokenRepository.saveAccessToken(accessToken, memberId, accessTokenExpirationSeconds)

        response.setHeader("Authorization", "Bearer $accessToken")

        val refreshCookie = authCookieManager.createRefreshTokenCookie(refreshToken, refreshTokenExpirationSeconds.toInt())
        response.addCookie(refreshCookie)

        return LoginResponse(
            expiresIn = accessTokenExpirationSeconds
        )
    }

    override fun logout(accessToken: String?, refreshToken: String?, response: HttpServletResponse) {
        accessToken?.let {
            val token = if (it.startsWith("Bearer ")) it.substring(7) else it
            try {
                val memberId = jwtTokenProvider.getMemberIdFromToken(token)
                redisTokenRepository.deleteAllUserTokens(memberId)
            } catch (e: Exception) {
                redisTokenRepository.invalidateAccessToken(token)
            }
        }

        refreshToken?.let {
            redisTokenRepository.deleteRefreshToken(it)
        }

        authCookieManager.clearRefreshTokenCookie(response)
    }
}
