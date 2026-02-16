package yi.memberapi.application.impl.auth.command

import io.mockk.*
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.security.MemberUserDetailsService
import yi.memberapi.adapter.webapi.dto.request.LoginRequest
import yi.memberapi.application.impl.auth.service.AuthCookieManager
import yi.memberapi.application.provided.RedisTokenRepository
import yi.memberapi.common.exception.AuthException
import yi.memberapi.common.util.JwtTokenProvider
import yi.memberapi.domain.member.Member

class CommandMemberAuthenticateTest {

    private lateinit var authenticationManager: AuthenticationManager
    private lateinit var userDetailsService: MemberUserDetailsService
    private lateinit var jwtTokenProvider: JwtTokenProvider
    private lateinit var redisTokenRepository: RedisTokenRepository
    private lateinit var authCookieManager: AuthCookieManager
    private lateinit var commandMemberAuthenticate: CommandMemberAuthenticate
    private lateinit var httpResponse: HttpServletResponse

    @BeforeEach
    fun setUp() {
        authenticationManager = mockk(relaxed = true)
        userDetailsService = mockk(relaxed = true)
        jwtTokenProvider = mockk(relaxed = true)
        redisTokenRepository = mockk(relaxed = true)
        authCookieManager = mockk(relaxed = true)
        httpResponse = mockk(relaxed = true)

        commandMemberAuthenticate = CommandMemberAuthenticate(
            authenticationManager,
            userDetailsService,
            jwtTokenProvider,
            redisTokenRepository,
            authCookieManager
        )
    }

    @Nested
    @DisplayName("로그인 테스트")
    inner class LoginTest {

        @Test
        @DisplayName("로그인을 성공적으로 처리한다")
        fun login_success() {
            val request = LoginRequest(
                username = "testuser",
                password = "password123",
                rememberMe = false
            )
            val clientIp = "192.168.1.1"
            val member = Member(
                id = 1L,
                username = "testuser",
                password = "encoded_password",
                name = "Test User"
            )
            val userDetails = MemberUserDetails(member)

            every { authenticationManager.authenticate(any()) } returns mockk()
            every { userDetailsService.loadUserByUsername(request.username) } returns userDetails
            every { jwtTokenProvider.generateAccessToken(userDetails, 1L) } returns "access-token"
            every { jwtTokenProvider.generateRefreshToken(userDetails, 1L, false) } returns "refresh-token"
            every { jwtTokenProvider.getRefreshTokenExpirationSeconds(false) } returns 604800L
            every { jwtTokenProvider.getAccessTokenExpirationSeconds() } returns 1800L
            every { authCookieManager.hashToken(any()) } returns "hashed-token"
            every { authCookieManager.createRefreshTokenCookie(any(), any()) } returns mockk(relaxed = true)

            val result = commandMemberAuthenticate.login(request, clientIp, httpResponse)

            assertEquals(1800L, result.expiresIn)

            verify { redisTokenRepository.deleteAllUserTokens(1L) }
            verify { redisTokenRepository.saveRefreshToken(any(), any(), any()) }
            verify { redisTokenRepository.saveAccessToken(any(), any(), any()) }
            verify { httpResponse.setHeader("Authorization", "Bearer access-token") }
            verify { httpResponse.addCookie(any()) }
        }

        @Test
        @DisplayName("rememberMe=true로 로그인 시 30일 토큰을 생성한다")
        fun login_withRememberMe() {
            val request = LoginRequest(
                username = "testuser",
                password = "password123",
                rememberMe = true
            )
            val clientIp = "192.168.1.1"
            val member = Member(
                id = 1L,
                username = "testuser",
                password = "encoded_password",
                name = "Test User"
            )
            val userDetails = MemberUserDetails(member)

            every { authenticationManager.authenticate(any()) } returns mockk()
            every { userDetailsService.loadUserByUsername(request.username) } returns userDetails
            every { jwtTokenProvider.generateAccessToken(userDetails, 1L) } returns "access-token"
            every { jwtTokenProvider.generateRefreshToken(userDetails, 1L, true) } returns "refresh-token"
            every { jwtTokenProvider.getRefreshTokenExpirationSeconds(true) } returns 2592000L
            every { jwtTokenProvider.getAccessTokenExpirationSeconds() } returns 1800L
            every { authCookieManager.hashToken(any()) } returns "hashed-token"
            every { authCookieManager.createRefreshTokenCookie(any(), any()) } returns mockk(relaxed = true)

            val result = commandMemberAuthenticate.login(request, clientIp, httpResponse)

            assertNotNull(result)
            verify { jwtTokenProvider.generateRefreshToken(userDetails, 1L, true) }
            verify { jwtTokenProvider.getRefreshTokenExpirationSeconds(true) }
        }

        @Test
        @DisplayName("잘못된 인증 정보로 로그인 시 예외가 발생한다")
        fun login_invalidCredentials() {
            val request = LoginRequest(
                username = "testuser",
                password = "wrong_password",
                rememberMe = false
            )
            val clientIp = "192.168.1.1"

            every { authenticationManager.authenticate(any()) } throws BadCredentialsException("Bad credentials")

            assertThrows<AuthException.InvalidCredentialsException> {
                commandMemberAuthenticate.login(request, clientIp, httpResponse)
            }
        }
    }

    @Nested
    @DisplayName("로그아웃 테스트")
    inner class LogoutTest {

        @Test
        @DisplayName("Access Token과 Refresh Token으로 로그아웃한다")
        fun logout_withBothTokens() {
            val accessToken = "Bearer access-token"
            val refreshToken = "refresh-token"

            every { jwtTokenProvider.getMemberIdFromToken("access-token") } returns 1L

            commandMemberAuthenticate.logout(accessToken, refreshToken, httpResponse)

            verify { redisTokenRepository.deleteAllUserTokens(1L) }
            verify { redisTokenRepository.deleteRefreshToken(refreshToken) }
            verify { authCookieManager.clearRefreshTokenCookie(httpResponse) }
        }

        @Test
        @DisplayName("Refresh Token만으로 로그아웃한다")
        fun logout_withRefreshTokenOnly() {
            val refreshToken = "refresh-token"

            commandMemberAuthenticate.logout(null, refreshToken, httpResponse)

            verify { redisTokenRepository.deleteRefreshToken(refreshToken) }
            verify { authCookieManager.clearRefreshTokenCookie(httpResponse) }
        }

        @Test
        @DisplayName("토큰 없이 로그아웃해도 예외가 발생하지 않는다")
        fun logout_withoutTokens() {
            assertDoesNotThrow {
                commandMemberAuthenticate.logout(null, null, httpResponse)
            }

            verify { authCookieManager.clearRefreshTokenCookie(httpResponse) }
        }
    }
}
