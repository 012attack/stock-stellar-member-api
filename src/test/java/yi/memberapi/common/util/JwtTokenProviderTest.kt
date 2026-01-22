package yi.memberapi.common.util

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import yi.memberapi.common.config.JwtProperties

class JwtTokenProviderTest {

    private lateinit var jwtTokenProvider: JwtTokenProvider
    private lateinit var jwtProperties: JwtProperties
    private lateinit var userDetails: UserDetails

    @BeforeEach
    fun setUp() {
        jwtProperties = JwtProperties(
            secret = "test-secret-key-must-be-at-least-32-characters-long-for-hs256",
            accessTokenExpiration = 1800000L,
            refreshTokenExpiration = 604800000L,
            refreshTokenExpirationRememberMe = 2592000000L
        )
        jwtTokenProvider = JwtTokenProvider(jwtProperties)

        userDetails = mockk<UserDetails>()
        every { userDetails.username } returns "testuser"
        every { userDetails.authorities } returns listOf(SimpleGrantedAuthority("ROLE_USER"))
    }

    @Nested
    @DisplayName("Access Token 생성 테스트")
    inner class GenerateAccessTokenTest {

        @Test
        @DisplayName("Access Token을 성공적으로 생성한다")
        fun generateAccessToken_success() {
            val memberId = 1L
            val token = jwtTokenProvider.generateAccessToken(userDetails, memberId)

            assertNotNull(token)
            assertTrue(token.isNotEmpty())
        }

        @Test
        @DisplayName("생성된 Access Token에서 username을 추출할 수 있다")
        fun generateAccessToken_extractUsername() {
            val memberId = 1L
            val token = jwtTokenProvider.generateAccessToken(userDetails, memberId)

            val extractedUsername = jwtTokenProvider.getUsernameFromToken(token)

            assertEquals("testuser", extractedUsername)
        }

        @Test
        @DisplayName("생성된 Access Token에서 memberId를 추출할 수 있다")
        fun generateAccessToken_extractMemberId() {
            val memberId = 1L
            val token = jwtTokenProvider.generateAccessToken(userDetails, memberId)

            val extractedMemberId = jwtTokenProvider.getMemberIdFromToken(token)

            assertEquals(memberId, extractedMemberId)
        }
    }

    @Nested
    @DisplayName("Refresh Token 생성 테스트")
    inner class GenerateRefreshTokenTest {

        @Test
        @DisplayName("rememberMe=false인 경우 7일 만료 토큰을 생성한다")
        fun generateRefreshToken_withoutRememberMe() {
            val memberId = 1L
            val token = jwtTokenProvider.generateRefreshToken(userDetails, memberId, false)

            assertNotNull(token)
            assertTrue(jwtTokenProvider.validateToken(token))
        }

        @Test
        @DisplayName("rememberMe=true인 경우 30일 만료 토큰을 생성한다")
        fun generateRefreshToken_withRememberMe() {
            val memberId = 1L
            val token = jwtTokenProvider.generateRefreshToken(userDetails, memberId, true)

            assertNotNull(token)
            assertTrue(jwtTokenProvider.validateToken(token))
        }
    }

    @Nested
    @DisplayName("Token 검증 테스트")
    inner class ValidateTokenTest {

        @Test
        @DisplayName("유효한 토큰은 true를 반환한다")
        fun validateToken_validToken_returnsTrue() {
            val token = jwtTokenProvider.generateAccessToken(userDetails, 1L)

            val result = jwtTokenProvider.validateToken(token)

            assertTrue(result)
        }

        @Test
        @DisplayName("잘못된 토큰은 false를 반환한다")
        fun validateToken_invalidToken_returnsFalse() {
            val result = jwtTokenProvider.validateToken("invalid.token.here")

            assertFalse(result)
        }

        @Test
        @DisplayName("빈 토큰은 false를 반환한다")
        fun validateToken_emptyToken_returnsFalse() {
            val result = jwtTokenProvider.validateToken("")

            assertFalse(result)
        }

        @Test
        @DisplayName("변조된 토큰은 false를 반환한다")
        fun validateToken_tamperedToken_returnsFalse() {
            val token = jwtTokenProvider.generateAccessToken(userDetails, 1L)
            val tamperedToken = token.dropLast(5) + "xxxxx"

            val result = jwtTokenProvider.validateToken(tamperedToken)

            assertFalse(result)
        }
    }

    @Nested
    @DisplayName("Token 만료 시간 테스트")
    inner class TokenExpirationTest {

        @Test
        @DisplayName("Access Token 만료 시간을 반환한다")
        fun getAccessTokenExpiration() {
            val expiration = jwtTokenProvider.getAccessTokenExpiration()

            assertEquals(1800000L, expiration)
        }

        @Test
        @DisplayName("rememberMe=false일 때 Refresh Token 만료 시간을 반환한다")
        fun getRefreshTokenExpiration_withoutRememberMe() {
            val expiration = jwtTokenProvider.getRefreshTokenExpiration(false)

            assertEquals(604800000L, expiration)
        }

        @Test
        @DisplayName("rememberMe=true일 때 Refresh Token 만료 시간을 반환한다")
        fun getRefreshTokenExpiration_withRememberMe() {
            val expiration = jwtTokenProvider.getRefreshTokenExpiration(true)

            assertEquals(2592000000L, expiration)
        }

        @Test
        @DisplayName("Access Token 만료 시간(초)을 반환한다")
        fun getAccessTokenExpirationSeconds() {
            val expirationSeconds = jwtTokenProvider.getAccessTokenExpirationSeconds()

            assertEquals(1800L, expirationSeconds)
        }

        @Test
        @DisplayName("Refresh Token 만료 시간(초)을 반환한다")
        fun getRefreshTokenExpirationSeconds() {
            val expirationSeconds = jwtTokenProvider.getRefreshTokenExpirationSeconds(false)

            assertEquals(604800L, expirationSeconds)
        }
    }

    @Nested
    @DisplayName("Token 정보 추출 테스트")
    inner class ExtractTokenInfoTest {

        @Test
        @DisplayName("토큰에서 만료 시간을 추출한다")
        fun getExpirationFromToken() {
            val token = jwtTokenProvider.generateAccessToken(userDetails, 1L)

            val expiration = jwtTokenProvider.getExpirationFromToken(token)

            assertNotNull(expiration)
            assertTrue(expiration.time > System.currentTimeMillis())
        }
    }
}
