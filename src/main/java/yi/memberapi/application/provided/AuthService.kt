package yi.memberapi.application.provided

import yi.memberapi.application.provided.dto.LoginRequest
import yi.memberapi.application.provided.dto.RefreshTokenRequest
import yi.memberapi.application.provided.dto.RegisterRequest
import yi.memberapi.application.provided.dto.RegisterResponse
import yi.memberapi.application.provided.dto.TokenResponse

interface AuthService {
    fun register(request: RegisterRequest): RegisterResponse
    fun login(request: LoginRequest): TokenResponse
    fun refresh(request: RefreshTokenRequest): TokenResponse
    fun logout(username: String)
}
