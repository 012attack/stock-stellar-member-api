package yi.memberapi.application.provided

import io.mockk.*
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.security.MemberUserDetailsService
import yi.memberapi.adapter.webapi.dto.LoginRequest
import yi.memberapi.adapter.webapi.dto.RegisterRequest
import yi.memberapi.application.required.MemberRepository
import yi.memberapi.application.required.RedisTokenRepository
import yi.memberapi.common.exception.AuthException
import yi.memberapi.common.util.JwtTokenProvider
import yi.memberapi.domain.member.Member
import yi.memberapi.domain.token.RefreshTokenInfo
import java.time.Instant
import java.util.*

class AuthServiceImplTest {

    private lateinit var authenticationManager: AuthenticationManager
    private lateinit var userDetailsService: MemberUserDetailsService
    private lateinit var jwtTokenProvider: JwtTokenProvider
    private lateinit var redisTokenRepository: RedisTokenRepository
    private lateinit var memberRepository: MemberRepository
    private lateinit var passwordEncoder: PasswordEncoder
    private lateinit var authService: AuthServiceImpl
    private lateinit var httpResponse: HttpServletResponse

    @BeforeEach
    fun setUp() {
        authenticationManager = mockk(relaxed = true)
        userDetailsService = mockk(relaxed = true)
        jwtTokenProvider = mockk(relaxed = true)
        redisTokenRepository = mockk(relaxed = true)
        memberRepository = mockk(relaxed = true)
        passwordEncoder = mockk(relaxed = true)
        httpResponse = mockk(relaxed = true)

        authService = AuthServiceImpl(
            authenticationManager,
            userDetailsService,
            jwtTokenProvider,
            redisTokenRepository,
            memberRepository,
            passwordEncoder
        )
    }

    @Nested
    @DisplayName("회원가입 테스트")
    inner class RegisterTest {

        @Test
        @DisplayName("회원가입을 성공적으로 처리한다")
        fun register_success() {
            val request = RegisterRequest(
                username = "newuser",
                password = "password123",
                name = "New User"
            )
            val savedMember = Member(
                id = 1L,
                username = "newuser",
                password = "encoded_password",
                name = "New User"
            )

            every { memberRepository.existsByUsername(request.username) } returns false
            every { passwordEncoder.encode(request.password) } returns "encoded_password"
            every { memberRepository.save(any()) } returns savedMember

            val result = authService.register(request)

            assertEquals(1L, result.id)
            assertEquals("newuser", result.username)
            assertEquals("New User", result.name)

            verify { memberRepository.existsByUsername(request.username) }
            verify { memberRepository.save(any()) }
        }

        @Test
        @DisplayName("이미 존재하는 username으로 회원가입 시 예외가 발생한다")
        fun register_usernameAlreadyExists() {
            val request = RegisterRequest(
                username = "existinguser",
                password = "password123",
                name = "Existing User"
            )

            every { memberRepository.existsByUsername(request.username) } returns true

            assertThrows<AuthException.UsernameAlreadyExistsException> {
                authService.register(request)
            }

            verify { memberRepository.existsByUsername(request.username) }
            verify(exactly = 0) { memberRepository.save(any()) }
        }
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

            val result = authService.login(request, clientIp, httpResponse)

            assertEquals("로그인 성공", result.message)
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

            val result = authService.login(request, clientIp, httpResponse)

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
                authService.login(request, clientIp, httpResponse)
            }
        }
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

            val result = authService.refresh(refreshToken, clientIp, httpResponse)

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
                authService.refresh(refreshToken, clientIp, httpResponse)
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
                authService.refresh(refreshToken, clientIp, httpResponse)
            }

            verify { redisTokenRepository.deleteRefreshToken(refreshToken) }
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
                authService.refresh(refreshToken, clientIp, httpResponse)
            }

            verify { redisTokenRepository.deleteAllUserTokens(1L) }
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
                authService.refresh(refreshToken, clientIp, httpResponse)
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

            authService.logout(accessToken, refreshToken, httpResponse)

            verify { redisTokenRepository.deleteAllUserTokens(1L) }
            verify { redisTokenRepository.deleteRefreshToken(refreshToken) }
            verify { httpResponse.addCookie(match { it.maxAge == 0 }) }
        }

        @Test
        @DisplayName("Refresh Token만으로 로그아웃한다")
        fun logout_withRefreshTokenOnly() {
            val refreshToken = "refresh-token"

            authService.logout(null, refreshToken, httpResponse)

            verify { redisTokenRepository.deleteRefreshToken(refreshToken) }
            verify { httpResponse.addCookie(match { it.maxAge == 0 }) }
        }

        @Test
        @DisplayName("토큰 없이 로그아웃해도 예외가 발생하지 않는다")
        fun logout_withoutTokens() {
            assertDoesNotThrow {
                authService.logout(null, null, httpResponse)
            }

            verify { httpResponse.addCookie(match { it.maxAge == 0 }) }
        }
    }
}
