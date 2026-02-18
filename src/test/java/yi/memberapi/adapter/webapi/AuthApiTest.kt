package yi.memberapi.adapter.webapi

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import yi.memberapi.adapter.webapi.auth.AuthApi
import yi.memberapi.adapter.webapi.auth.dto.request.LoginRequest
import yi.memberapi.adapter.webapi.auth.dto.request.RegisterRequest
import yi.memberapi.adapter.webapi.auth.dto.response.LoginResponse
import yi.memberapi.adapter.webapi.auth.dto.response.RefreshResponse
import yi.memberapi.adapter.webapi.auth.dto.response.RegisterResponse
import yi.memberapi.application.required.MemberAuthenticator
import yi.memberapi.application.required.MemberRegister
import yi.memberapi.application.required.TokenRefresher
import yi.memberapi.common.exception.AuthException
import yi.memberapi.common.exception.GlobalExceptionHandler

@WebMvcTest(AuthApi::class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler::class, AuthApiTest.TestConfig::class)
class AuthApiTest {

    @TestConfiguration
    class TestConfig {
        @Bean
        fun memberRegister(): MemberRegister = mockk(relaxed = true)

        @Bean
        fun memberAuthenticator(): MemberAuthenticator = mockk(relaxed = true)

        @Bean
        fun tokenRefresher(): TokenRefresher = mockk(relaxed = true)

        @Bean
        fun jwtTokenProvider(): yi.memberapi.common.util.JwtTokenProvider = mockk(relaxed = true)

        @Bean
        fun memberUserDetailsService(): yi.memberapi.adapter.security.MemberUserDetailsService = mockk(relaxed = true)

        @Bean
        fun redisTokenRepository(): yi.memberapi.application.provided.RedisTokenRepository = mockk(relaxed = true)
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val objectMapper: ObjectMapper = ObjectMapper()

    @Autowired
    private lateinit var memberRegister: MemberRegister

    @Autowired
    private lateinit var memberAuthenticator: MemberAuthenticator

    @Autowired
    private lateinit var tokenRefresher: TokenRefresher

    @Nested
    @DisplayName("POST /api/auth/register")
    inner class RegisterEndpointTest {

        @Test
        @DisplayName("회원가입 성공 시 201을 반환한다")
        fun register_success() {
            val request = RegisterRequest(
                username = "newuser",
                password = "password123",
                name = "New User"
            )
            val response = RegisterResponse(
                id = 1L,
                username = "newuser",
                name = "New User"
            )

            every { memberRegister.register(request) } returns response

            mockMvc.perform(
                post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isCreated)
                .andExpect(header().string("Location", "/api/members/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.name").value("New User"))
        }

        @Test
        @DisplayName("이미 존재하는 username으로 회원가입 시 409를 반환한다")
        fun register_usernameAlreadyExists() {
            val request = RegisterRequest(
                username = "existinguser",
                password = "password123",
                name = "Existing User"
            )

            every { memberRegister.register(request) } throws AuthException.UsernameAlreadyExistsException()

            mockMvc.perform(
                post("/api/auth/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isConflict)
        }
    }

    @Nested
    @DisplayName("POST /api/auth/login")
    inner class LoginEndpointTest {

        @Test
        @DisplayName("로그인 성공 시 200을 반환한다")
        fun login_success() {
            val request = LoginRequest(
                username = "testuser",
                password = "password123",
                rememberMe = false
            )
            val response = LoginResponse(
                message = "로그인 성공",
                expiresIn = 1800L
            )

            every { memberAuthenticator.login(request, any(), any()) } returns response

            mockMvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("X-Forwarded-For", "192.168.1.1")
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.message").value("로그인 성공"))
                .andExpect(jsonPath("$.expiresIn").value(1800))
        }

        @Test
        @DisplayName("잘못된 인증 정보로 로그인 시 401을 반환한다")
        fun login_invalidCredentials() {
            val request = LoginRequest(
                username = "testuser",
                password = "wrong_password",
                rememberMe = false
            )

            every { memberAuthenticator.login(request, any(), any()) } throws AuthException.InvalidCredentialsException()

            mockMvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isUnauthorized)
        }

        @Test
        @DisplayName("rememberMe=true로 로그인할 수 있다")
        fun login_withRememberMe() {
            val request = LoginRequest(
                username = "testuser",
                password = "password123",
                rememberMe = true
            )
            val response = LoginResponse(
                message = "로그인 성공",
                expiresIn = 1800L
            )

            every { memberAuthenticator.login(request, any(), any()) } returns response

            mockMvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            )
                .andExpect(status().isOk)
        }
    }

    @Nested
    @DisplayName("POST /api/auth/refresh")
    inner class RefreshEndpointTest {

        @Test
        @DisplayName("토큰 갱신 성공 시 200을 반환한다")
        fun refresh_success() {
            val response = RefreshResponse(
                message = "토큰 갱신 성공",
                expiresIn = 1800L
            )

            every { tokenRefresher.refresh("valid-refresh-token", any(), any()) } returns response

            mockMvc.perform(
                post("/api/auth/refresh")
                    .cookie(Cookie("refreshToken", "valid-refresh-token"))
                    .header("X-Forwarded-For", "192.168.1.1")
            )
                .andExpect(status().isOk)
                .andExpect(jsonPath("$.message").value("토큰 갱신 성공"))
                .andExpect(jsonPath("$.expiresIn").value(1800))
        }

        @Test
        @DisplayName("Refresh Token이 없으면 400을 반환한다")
        fun refresh_missingCookie() {
            mockMvc.perform(
                post("/api/auth/refresh")
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        @DisplayName("존재하지 않는 Refresh Token으로 갱신 시 401을 반환한다")
        fun refresh_tokenNotFound() {
            every { tokenRefresher.refresh("invalid-token", any(), any()) } throws AuthException.RefreshTokenNotFoundException()

            mockMvc.perform(
                post("/api/auth/refresh")
                    .cookie(Cookie("refreshToken", "invalid-token"))
            )
                .andExpect(status().isUnauthorized)
        }

        @Test
        @DisplayName("만료된 Refresh Token으로 갱신 시 401을 반환한다")
        fun refresh_tokenExpired() {
            every { tokenRefresher.refresh("expired-token", any(), any()) } throws AuthException.TokenExpiredException()

            mockMvc.perform(
                post("/api/auth/refresh")
                    .cookie(Cookie("refreshToken", "expired-token"))
            )
                .andExpect(status().isUnauthorized)
        }

        @Test
        @DisplayName("IP가 일치하지 않으면 403을 반환한다")
        fun refresh_ipMismatch() {
            every { tokenRefresher.refresh("valid-token", any(), any()) } throws AuthException.IpMismatchException()

            mockMvc.perform(
                post("/api/auth/refresh")
                    .cookie(Cookie("refreshToken", "valid-token"))
                    .header("X-Forwarded-For", "192.168.1.100")
            )
                .andExpect(status().isForbidden)
        }
    }

    @Nested
    @DisplayName("POST /api/auth/logout")
    inner class LogoutEndpointTest {

        @Test
        @DisplayName("로그아웃 성공 시 204를 반환한다")
        fun logout_success() {
            every { memberAuthenticator.logout(any(), any(), any()) } returns Unit

            mockMvc.perform(
                post("/api/auth/logout")
                    .header("Authorization", "Bearer access-token")
                    .cookie(Cookie("refreshToken", "refresh-token"))
            )
                .andExpect(status().isNoContent)

            verify { memberAuthenticator.logout("Bearer access-token", "refresh-token", any()) }
        }

        @Test
        @DisplayName("토큰 없이 로그아웃해도 204를 반환한다")
        fun logout_withoutTokens() {
            every { memberAuthenticator.logout(any(), any(), any()) } returns Unit

            mockMvc.perform(
                post("/api/auth/logout")
            )
                .andExpect(status().isNoContent)
        }
    }

    @Nested
    @DisplayName("클라이언트 IP 추출 테스트")
    inner class ClientIpExtractionTest {

        @Test
        @DisplayName("X-Forwarded-For 헤더에서 IP를 추출한다")
        fun extractClientIp_fromXForwardedFor() {
            val request = LoginRequest(
                username = "testuser",
                password = "password123",
                rememberMe = false
            )
            val response = LoginResponse(expiresIn = 1800L)

            every { memberAuthenticator.login(request, "192.168.1.1", any()) } returns response

            mockMvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("X-Forwarded-For", "192.168.1.1, 10.0.0.1")
            )
                .andExpect(status().isOk)

            verify { memberAuthenticator.login(request, "192.168.1.1", any()) }
        }

        @Test
        @DisplayName("X-Real-IP 헤더에서 IP를 추출한다")
        fun extractClientIp_fromXRealIp() {
            val request = LoginRequest(
                username = "testuser",
                password = "password123",
                rememberMe = false
            )
            val response = LoginResponse(expiresIn = 1800L)

            every { memberAuthenticator.login(request, "10.0.0.1", any()) } returns response

            mockMvc.perform(
                post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .header("X-Real-IP", "10.0.0.1")
            )
                .andExpect(status().isOk)

            verify { memberAuthenticator.login(request, "10.0.0.1", any()) }
        }
    }
}
