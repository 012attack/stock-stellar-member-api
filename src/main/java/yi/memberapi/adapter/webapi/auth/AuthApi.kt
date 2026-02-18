package yi.memberapi.adapter.webapi.auth

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.application.required.MemberAuthenticator
import yi.memberapi.application.required.MemberRegister
import yi.memberapi.application.required.TokenRefresher
import yi.memberapi.adapter.webapi.auth.dto.request.LoginRequest
import yi.memberapi.adapter.webapi.auth.dto.request.RegisterRequest
import yi.memberapi.adapter.webapi.auth.dto.response.LoginResponse
import yi.memberapi.adapter.webapi.auth.dto.response.RefreshResponse
import yi.memberapi.adapter.webapi.auth.dto.response.RegisterResponse
import java.net.URI

@RestController
@RequestMapping("/api/auth")
class AuthApi(
    private val memberRegister: MemberRegister,
    private val memberAuthenticator: MemberAuthenticator,
    private val tokenRefresher: TokenRefresher
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<RegisterResponse> {
        val response = memberRegister.register(request)
        return ResponseEntity.created(URI.create("/api/members/${response.id}")).body(response)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest,
        httpRequest: HttpServletRequest,
        httpResponse: HttpServletResponse
    ): ResponseEntity<LoginResponse> {
        val clientIp = extractClientIp(httpRequest)
        val response = memberAuthenticator.login(request, clientIp, httpResponse)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/refresh")
    fun refresh(
        @CookieValue(name = "refreshToken", required = true) refreshToken: String,
        httpRequest: HttpServletRequest,
        httpResponse: HttpServletResponse
    ): ResponseEntity<RefreshResponse> {
        val clientIp = extractClientIp(httpRequest)
        val response = tokenRefresher.refresh(refreshToken, clientIp, httpResponse)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/logout")
    fun logout(
        @RequestHeader(name = "Authorization", required = false) authorization: String?,
        @CookieValue(name = "refreshToken", required = false) refreshToken: String?,
        httpResponse: HttpServletResponse
    ): ResponseEntity<Unit> {
        memberAuthenticator.logout(authorization, refreshToken, httpResponse)
        return ResponseEntity.noContent().build()
    }

    private fun extractClientIp(request: HttpServletRequest): String {
        val xForwardedFor = request.getHeader("X-Forwarded-For")
        if (!xForwardedFor.isNullOrBlank()) {
            return xForwardedFor.split(",").first().trim()
        }

        val xRealIp = request.getHeader("X-Real-IP")
        if (!xRealIp.isNullOrBlank()) {
            return xRealIp
        }

        return request.remoteAddr ?: "unknown"
    }
}
