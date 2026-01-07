package yi.memberapi.adapter.webapi

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.application.provided.AuthService
import yi.memberapi.application.provided.dto.LoginRequest
import yi.memberapi.application.provided.dto.RefreshTokenRequest
import yi.memberapi.application.provided.dto.RegisterRequest
import yi.memberapi.application.provided.dto.RegisterResponse
import yi.memberapi.application.provided.dto.TokenResponse
import java.net.URI

@RestController
@RequestMapping("/api/auth")
class AuthApi(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<RegisterResponse> {
        val response = authService.register(request)
        return ResponseEntity.created(URI.create("/api/members/${response.id}")).body(response)
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<TokenResponse> {
        val response = authService.login(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/refresh")
    fun refresh(@RequestBody request: RefreshTokenRequest): ResponseEntity<TokenResponse> {
        val response = authService.refresh(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/logout")
    fun logout(@AuthenticationPrincipal userDetails: MemberUserDetails): ResponseEntity<Unit> {
        authService.logout(userDetails.username)
        return ResponseEntity.noContent().build()
    }
}
