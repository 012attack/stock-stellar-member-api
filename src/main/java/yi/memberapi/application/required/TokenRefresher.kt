package yi.memberapi.application.required

import jakarta.servlet.http.HttpServletResponse
import yi.memberapi.adapter.webapi.dto.response.RefreshResponse

interface TokenRefresher {
    fun refresh(refreshToken: String, clientIp: String, response: HttpServletResponse): RefreshResponse
}
