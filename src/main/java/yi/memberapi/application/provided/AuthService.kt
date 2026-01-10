package yi.memberapi.application.provided

import yi.memberapi.adapter.webapi.dto.LoginRequest
import yi.memberapi.adapter.webapi.dto.RefreshTokenRequest
import yi.memberapi.adapter.webapi.dto.RegisterRequest
import yi.memberapi.adapter.webapi.dto.RegisterResponse
import yi.memberapi.adapter.webapi.dto.TokenResponse

interface AuthService {
    fun register(request: RegisterRequest): RegisterResponse
    fun login(request: LoginRequest): TokenResponse
    fun refresh(request: RefreshTokenRequest): TokenResponse
    fun logout(username: String)
}
