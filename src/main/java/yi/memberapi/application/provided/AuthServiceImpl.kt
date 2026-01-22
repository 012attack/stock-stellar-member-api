package yi.memberapi.application.provided

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.security.MemberUserDetailsService
import yi.memberapi.adapter.webapi.dto.LoginRequest
import yi.memberapi.adapter.webapi.dto.LoginResponse
import yi.memberapi.adapter.webapi.dto.RefreshResponse
import yi.memberapi.adapter.webapi.dto.RegisterRequest
import yi.memberapi.adapter.webapi.dto.RegisterResponse
import yi.memberapi.application.required.MemberRepository
import yi.memberapi.application.required.RedisTokenRepository
import yi.memberapi.common.exception.AuthException
import yi.memberapi.common.util.JwtTokenProvider
import yi.memberapi.domain.member.Member
import yi.memberapi.domain.token.RefreshTokenInfo
import java.security.MessageDigest
import java.time.Instant
import java.util.*

@Service
@Transactional
class AuthServiceImpl(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: MemberUserDetailsService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val redisTokenRepository: RedisTokenRepository,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: org.springframework.security.crypto.password.PasswordEncoder
) : AuthService {

    companion object {
        private const val REFRESH_TOKEN_COOKIE_NAME = "refreshToken"
        private const val REFRESH_TOKEN_COOKIE_PATH = "/member-api/api/auth"
    }

    override fun register(request: RegisterRequest): RegisterResponse {
        if (memberRepository.existsByUsername(request.username)) {
            throw AuthException.UsernameAlreadyExistsException()
        }

        val member = Member(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            name = request.name
        )
        val savedMember = memberRepository.save(member)

        return RegisterResponse(
            id = savedMember.id!!,
            username = savedMember.username!!,
            name = savedMember.name!!
        )
    }

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
            tokenHash = hashToken(refreshToken),
            clientIp = clientIp,
            rememberMe = request.rememberMe,
            expiresAt = Instant.now().plusSeconds(refreshTokenExpirationSeconds).toEpochMilli()
        )
        redisTokenRepository.saveRefreshToken(refreshToken, refreshTokenInfo, refreshTokenExpirationSeconds)

        val accessTokenExpirationSeconds = jwtTokenProvider.getAccessTokenExpirationSeconds()
        redisTokenRepository.saveAccessToken(accessToken, memberId, accessTokenExpirationSeconds)

        response.setHeader("Authorization", "Bearer $accessToken")

        val refreshCookie = createRefreshTokenCookie(refreshToken, refreshTokenExpirationSeconds.toInt())
        response.addCookie(refreshCookie)

        return LoginResponse(
            expiresIn = accessTokenExpirationSeconds
        )
    }

    override fun refresh(refreshToken: String, clientIp: String, response: HttpServletResponse): RefreshResponse {
        val tokenInfo = redisTokenRepository.findRefreshToken(refreshToken)
            ?: throw AuthException.RefreshTokenNotFoundException()

        if (tokenInfo.isExpired()) {
            redisTokenRepository.deleteRefreshToken(refreshToken)
            clearRefreshTokenCookie(response)
            throw AuthException.TokenExpiredException()
        }

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            redisTokenRepository.deleteRefreshToken(refreshToken)
            clearRefreshTokenCookie(response)
            throw AuthException.InvalidTokenException()
        }

        if (tokenInfo.clientIp != clientIp) {
            redisTokenRepository.deleteAllUserTokens(tokenInfo.memberId)
            clearRefreshTokenCookie(response)
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
            tokenHash = hashToken(newRefreshToken),
            clientIp = clientIp,
            rememberMe = tokenInfo.rememberMe,
            expiresAt = Instant.now().plusSeconds(refreshTokenExpirationSeconds).toEpochMilli()
        )
        redisTokenRepository.saveRefreshToken(newRefreshToken, newTokenInfo, refreshTokenExpirationSeconds)

        val accessTokenExpirationSeconds = jwtTokenProvider.getAccessTokenExpirationSeconds()
        redisTokenRepository.saveAccessToken(newAccessToken, tokenInfo.memberId, accessTokenExpirationSeconds)

        response.setHeader("Authorization", "Bearer $newAccessToken")

        val refreshCookie = createRefreshTokenCookie(newRefreshToken, refreshTokenExpirationSeconds.toInt())
        response.addCookie(refreshCookie)

        return RefreshResponse(
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

        clearRefreshTokenCookie(response)
    }

    private fun createRefreshTokenCookie(token: String, maxAgeSeconds: Int): Cookie {
        return Cookie(REFRESH_TOKEN_COOKIE_NAME, token).apply {
            isHttpOnly = true
            secure = true
            path = REFRESH_TOKEN_COOKIE_PATH
            maxAge = maxAgeSeconds
            setAttribute("SameSite", "Strict")
        }
    }

    private fun clearRefreshTokenCookie(response: HttpServletResponse) {
        val cookie = Cookie(REFRESH_TOKEN_COOKIE_NAME, "").apply {
            isHttpOnly = true
            secure = true
            path = REFRESH_TOKEN_COOKIE_PATH
            maxAge = 0
            setAttribute("SameSite", "Strict")
        }
        response.addCookie(cookie)
    }

    private fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.toByteArray())
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes)
    }
}
