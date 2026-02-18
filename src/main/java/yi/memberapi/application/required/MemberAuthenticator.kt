package yi.memberapi.application.required

import jakarta.servlet.http.HttpServletResponse
import yi.memberapi.adapter.webapi.auth.dto.request.LoginRequest
import yi.memberapi.adapter.webapi.auth.dto.response.LoginResponse

interface MemberAuthenticator {
    fun login(request: LoginRequest, clientIp: String, response: HttpServletResponse): LoginResponse
    fun logout(accessToken: String?, refreshToken: String?, response: HttpServletResponse)
}
