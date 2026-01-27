package yi.memberapi.application.auth.service

import io.mockk.*
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.security.MemberUserDetailsService
import yi.memberapi.application.provided.RedisTokenRepository
import yi.memberapi.common.exception.AuthException
import yi.memberapi.common.util.JwtTokenProvider
import yi.memberapi.domain.member.Member
import yi.memberapi.domain.token.RefreshTokenInfo
import java.time.Instant

class TokenRefresherImplTest {

    private lateinit var userDetailsService: MemberUserDetailsService
    private lateinit var jwtTokenProvider: JwtTokenProvider
    private lateinit var redisTokenRepository: RedisTokenRepository
    private lateinit var authCookieManager: AuthCookieManager
    private lateinit var tokenRefresher: TokenRefresherImpl
    private lateinit var httpResponse: HttpServletResponse

    @BeforeEach
    fun setUp() {
        userDetailsService = mockk(relaxed = true)
        jwtTokenProvider = mockk(relaxed = true)
        redisTokenRepository = mockk(relaxed = true)
        authCookieManager = mockk(relaxed = true)
        httpResponse = mockk(relaxed = true)

        tokenRefresher = TokenRefresherImpl(
            userDetailsService,
            jwtTokenProvider,
            redisTokenRepository,
            authCookieManager
        )
    }

    @Nested
    @DisplayName("토큰 갱신 테스트")
    inner class RefreshTest {

        @Test
        @DisplayName("토큰 갱신을 성공적으로 처리한다")
        fun refresh_success() {
            val refreshToken = "valid-refresh-token"
            val clientIp = "192.168.1.1"
            val tokenInfo = RefreshTokenInfo(
                memberId = 1L,
                username = "testuser",
                tokenHash = "hash",
                clientIp = clientIp,
                rememberMe = false,
                expiresAt = Instant.now().plusSeconds(604800).toEpochMilli()
            )
            val member = Member(
                id = 1L,
                username = "testuser",
                password = "encoded_password",
                name = "Test User"
            )
            val userDetails = MemberUserDetails(member)

            every { redisTokenRepository.findRefreshToken(refreshToken) } returns tokenInfo
            every { jwtTokenProvider.validateToken(refreshToken) } returns true
            every { userDetailsService.loadUserByUsername("testuser") } returns userDetails
            every { jwtTokenProvider.generateAccessToken(userDetails, 1L) } returns "new-access-token"
            every { jwtTokenProvider.generateRefreshToken(userDetails, 1L, false) } returns "new-refresh-token"
            every { jwtTokenProvider.getRefreshTokenExpirationSeconds(false) } returns 604800L
            every { jwtTokenProvider.getAccessTokenExpirationSeconds() } returns 1800L
            every { authCookieManager.hashToken(any()) } returns "new-hashed-token"
            every { authCookieManager.createRefreshTokenCookie(any(), any()) } returns mockk(relaxed = true)

            val result = tokenRefresher.refresh(refreshToken, clientIp, httpResponse)

            assertEquals("토큰 갱신 성공", result.message)
            assertEquals(1800L, result.expiresIn)

            verify { redisTokenRepository.deleteRefreshToken(refreshToken) }
            verify { redisTokenRepository.saveRefreshToken(any(), any(), any()) }
            verify { httpResponse.setHeader("Authorization", "Bearer new-access-token") }
        }

        @Test
        @DisplayName("존재하지 않는 Refresh Token으로 갱신 시 예외가 발생한다")
        fun refresh_tokenNotFound() {
            val refreshToken = "non-existent-token"
            val clientIp = "192.168.1.1"

            every { redisTokenRepository.findRefreshToken(refreshToken) } returns null

            assertThrows<AuthException.RefreshTokenNotFoundException> {
                tokenRefresher.refresh(refreshToken, clientIp, httpResponse)
            }
        }

        @Test
        @DisplayName("만료된 Refresh Token으로 갱신 시 예외가 발생한다")
        fun refresh_tokenExpired() {
            val refreshToken = "expired-token"
            val clientIp = "192.168.1.1"
            val tokenInfo = RefreshTokenInfo(
                memberId = 1L,
                username = "testuser",
                tokenHash = "hash",
                clientIp = clientIp,
                rememberMe = false,
                expiresAt = Instant.now().minusSeconds(3600).toEpochMilli()
            )

            every { redisTokenRepository.findRefreshToken(refreshToken) } returns tokenInfo

            assertThrows<AuthException.TokenExpiredException> {
                tokenRefresher.refresh(refreshToken, clientIp, httpResponse)
            }

            verify { redisTokenRepository.deleteRefreshToken(refreshToken) }
            verify { authCookieManager.clearRefreshTokenCookie(httpResponse) }
        }

        @Test
        @DisplayName("IP가 일치하지 않으면 예외가 발생한다")
        fun refresh_ipMismatch() {
            val refreshToken = "valid-refresh-token"
            val clientIp = "192.168.1.100"
            val tokenInfo = RefreshTokenInfo(
                memberId = 1L,
                username = "testuser",
                tokenHash = "hash",
                clientIp = "192.168.1.1",
                rememberMe = false,
                expiresAt = Instant.now().plusSeconds(604800).toEpochMilli()
            )

            every { redisTokenRepository.findRefreshToken(refreshToken) } returns tokenInfo
            every { jwtTokenProvider.validateToken(refreshToken) } returns true

            assertThrows<AuthException.IpMismatchException> {
                tokenRefresher.refresh(refreshToken, clientIp, httpResponse)
            }

            verify { redisTokenRepository.deleteAllUserTokens(1L) }
            verify { authCookieManager.clearRefreshTokenCookie(httpResponse) }
        }

        @Test
        @DisplayName("유효하지 않은 JWT로 갱신 시 예외가 발생한다")
        fun refresh_invalidJwt() {
            val refreshToken = "invalid-jwt-token"
            val clientIp = "192.168.1.1"
            val tokenInfo = RefreshTokenInfo(
                memberId = 1L,
                username = "testuser",
                tokenHash = "hash",
                clientIp = clientIp,
                rememberMe = false,
                expiresAt = Instant.now().plusSeconds(604800).toEpochMilli()
            )

            every { redisTokenRepository.findRefreshToken(refreshToken) } returns tokenInfo
            every { jwtTokenProvider.validateToken(refreshToken) } returns false

            assertThrows<AuthException.InvalidTokenException> {
                tokenRefresher.refresh(refreshToken, clientIp, httpResponse)
            }

            verify { redisTokenRepository.deleteRefreshToken(refreshToken) }
            verify { authCookieManager.clearRefreshTokenCookie(httpResponse) }
        }
    }
}
