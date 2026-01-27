package yi.memberapi.adapter.security

import io.mockk.*
import jakarta.servlet.FilterChain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import yi.memberapi.application.provided.RedisTokenRepository
import yi.memberapi.common.util.JwtTokenProvider
import yi.memberapi.domain.member.Member

class JwtAuthenticationFilterTest {

    private lateinit var jwtTokenProvider: JwtTokenProvider
    private lateinit var userDetailsService: MemberUserDetailsService
    private lateinit var redisTokenRepository: RedisTokenRepository
    private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    private lateinit var request: MockHttpServletRequest
    private lateinit var response: MockHttpServletResponse
    private lateinit var filterChain: FilterChain

    @BeforeEach
    fun setUp() {
        jwtTokenProvider = mockk(relaxed = true)
        userDetailsService = mockk(relaxed = true)
        redisTokenRepository = mockk(relaxed = true)

        jwtAuthenticationFilter = JwtAuthenticationFilter(
            jwtTokenProvider,
            userDetailsService,
            redisTokenRepository
        )

        request = MockHttpServletRequest()
        response = MockHttpServletResponse()
        filterChain = mockk(relaxed = true)

        SecurityContextHolder.clearContext()
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    @Nested
    @DisplayName("토큰 추출 테스트")
    inner class TokenExtractionTest {

        @Test
        @DisplayName("Authorization 헤더에서 Bearer 토큰을 추출한다")
        fun extractToken_validBearerToken() {
            val validToken = "valid-jwt-token"
            val member = Member(id = 1L, username = "testuser", password = "password", name = "Test")
            val userDetails = MemberUserDetails(member)

            request.addHeader("Authorization", "Bearer $validToken")
            every { jwtTokenProvider.validateToken(validToken) } returns true
            every { redisTokenRepository.isAccessTokenValid(validToken) } returns true
            every { jwtTokenProvider.getUsernameFromToken(validToken) } returns "testuser"
            every { userDetailsService.loadUserByUsername("testuser") } returns userDetails

            jwtAuthenticationFilter.doFilter(request, response, filterChain)

            assertNotNull(SecurityContextHolder.getContext().authentication)
            assertEquals("testuser", SecurityContextHolder.getContext().authentication!!.name)

            verify { filterChain.doFilter(request, response) }
        }

        @Test
        @DisplayName("Authorization 헤더가 없으면 인증을 설정하지 않는다")
        fun extractToken_noAuthorizationHeader() {
            // No Authorization header set

            jwtAuthenticationFilter.doFilter(request, response, filterChain)

            assertNull(SecurityContextHolder.getContext().authentication)
            verify { filterChain.doFilter(request, response) }
        }

        @Test
        @DisplayName("Bearer로 시작하지 않는 토큰은 무시한다")
        fun extractToken_nonBearerToken() {
            request.addHeader("Authorization", "Basic credentials")

            jwtAuthenticationFilter.doFilter(request, response, filterChain)

            assertNull(SecurityContextHolder.getContext().authentication)
            verify { filterChain.doFilter(request, response) }
        }
    }

    @Nested
    @DisplayName("토큰 검증 테스트")
    inner class TokenValidationTest {

        @Test
        @DisplayName("유효하지 않은 JWT는 인증을 설정하지 않는다")
        fun validateToken_invalidJwt() {
            val invalidToken = "invalid-jwt-token"

            request.addHeader("Authorization", "Bearer $invalidToken")
            every { jwtTokenProvider.validateToken(invalidToken) } returns false

            jwtAuthenticationFilter.doFilter(request, response, filterChain)

            assertNull(SecurityContextHolder.getContext().authentication)
            verify { filterChain.doFilter(request, response) }
            verify(exactly = 0) { userDetailsService.loadUserByUsername(any()) }
        }

        @Test
        @DisplayName("Redis에 없는 토큰은 인증을 설정하지 않는다")
        fun validateToken_notInRedis() {
            val token = "token-not-in-redis"

            request.addHeader("Authorization", "Bearer $token")
            every { jwtTokenProvider.validateToken(token) } returns true
            every { redisTokenRepository.isAccessTokenValid(token) } returns false

            jwtAuthenticationFilter.doFilter(request, response, filterChain)

            assertNull(SecurityContextHolder.getContext().authentication)
            verify { filterChain.doFilter(request, response) }
            verify(exactly = 0) { userDetailsService.loadUserByUsername(any()) }
        }

        @Test
        @DisplayName("JWT와 Redis 모두 유효한 토큰만 인증을 설정한다")
        fun validateToken_validJwtAndRedis() {
            val validToken = "valid-token"
            val member = Member(id = 1L, username = "testuser", password = "password", name = "Test")
            val userDetails = MemberUserDetails(member)

            request.addHeader("Authorization", "Bearer $validToken")
            every { jwtTokenProvider.validateToken(validToken) } returns true
            every { redisTokenRepository.isAccessTokenValid(validToken) } returns true
            every { jwtTokenProvider.getUsernameFromToken(validToken) } returns "testuser"
            every { userDetailsService.loadUserByUsername("testuser") } returns userDetails

            jwtAuthenticationFilter.doFilter(request, response, filterChain)

            assertNotNull(SecurityContextHolder.getContext().authentication)
            verify { filterChain.doFilter(request, response) }
        }
    }

    @Nested
    @DisplayName("인증 설정 테스트")
    inner class AuthenticationSettingTest {

        @Test
        @DisplayName("유효한 토큰으로 인증이 성공하면 SecurityContext에 인증 정보가 설정된다")
        fun setAuthentication_success() {
            val validToken = "valid-token"
            val member = Member(id = 1L, username = "testuser", password = "password", name = "Test User")
            val userDetails = MemberUserDetails(member)

            request.addHeader("Authorization", "Bearer $validToken")
            every { jwtTokenProvider.validateToken(validToken) } returns true
            every { redisTokenRepository.isAccessTokenValid(validToken) } returns true
            every { jwtTokenProvider.getUsernameFromToken(validToken) } returns "testuser"
            every { userDetailsService.loadUserByUsername("testuser") } returns userDetails

            jwtAuthenticationFilter.doFilter(request, response, filterChain)

            val authentication = SecurityContextHolder.getContext().authentication
            assertNotNull(authentication)
            assertEquals("testuser", authentication!!.name)
            assertTrue(authentication.isAuthenticated)
            assertNotNull(authentication.authorities)
        }

        @Test
        @DisplayName("필터 체인은 항상 호출된다")
        fun filterChain_alwaysCalled() {
            // No Authorization header set

            jwtAuthenticationFilter.doFilter(request, response, filterChain)

            verify(exactly = 1) { filterChain.doFilter(request, response) }
        }

        @Test
        @DisplayName("인증 실패 시에도 필터 체인은 호출된다")
        fun filterChain_calledOnAuthenticationFailure() {
            request.addHeader("Authorization", "Bearer invalid-token")
            every { jwtTokenProvider.validateToken("invalid-token") } returns false

            jwtAuthenticationFilter.doFilter(request, response, filterChain)

            assertNull(SecurityContextHolder.getContext().authentication)
            verify(exactly = 1) { filterChain.doFilter(request, response) }
        }
    }
}
