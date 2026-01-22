package yi.memberapi.application.provided

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import yi.memberapi.adapter.webapi.dto.LoginRequest
import yi.memberapi.adapter.webapi.dto.LoginResponse
import yi.memberapi.adapter.webapi.dto.RefreshResponse
import yi.memberapi.adapter.webapi.dto.RegisterRequest
import yi.memberapi.adapter.webapi.dto.RegisterResponse

interface AuthService {
    fun register(request: RegisterRequest): RegisterResponse
    fun login(request: LoginRequest, clientIp: String, response: HttpServletResponse): LoginResponse
    fun refresh(refreshToken: String, clientIp: String, response: HttpServletResponse): RefreshResponse
    fun logout(accessToken: String?, refreshToken: String?, response: HttpServletResponse)
}
