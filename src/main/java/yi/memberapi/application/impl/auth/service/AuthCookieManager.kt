package yi.memberapi.application.impl.auth.service

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.util.*

@Component
class AuthCookieManager {

    companion object {
        private const val REFRESH_TOKEN_COOKIE_NAME = "refreshToken"
        private const val REFRESH_TOKEN_COOKIE_PATH = "/member-api/api/auth"
    }

    fun createRefreshTokenCookie(token: String, maxAgeSeconds: Int): Cookie {
        return Cookie(REFRESH_TOKEN_COOKIE_NAME, token).apply {
            isHttpOnly = true
            secure = true
            path = REFRESH_TOKEN_COOKIE_PATH
            maxAge = maxAgeSeconds
            setAttribute("SameSite", "Strict")
        }
    }

    fun clearRefreshTokenCookie(response: HttpServletResponse) {
        val cookie = Cookie(REFRESH_TOKEN_COOKIE_NAME, "").apply {
            isHttpOnly = true
            secure = true
            path = REFRESH_TOKEN_COOKIE_PATH
            maxAge = 0
            setAttribute("SameSite", "Strict")
        }
        response.addCookie(cookie)
    }

    fun hashToken(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(token.toByteArray())
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes)
    }
}
