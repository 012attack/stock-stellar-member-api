package yi.memberapi.application.required

import jakarta.servlet.http.HttpServletResponse
import yi.memberapi.adapter.webapi.auth.dto.response.RefreshResponse

interface TokenRefresher {
    fun refresh(refreshToken: String, clientIp: String, response: HttpServletResponse): RefreshResponse
}
