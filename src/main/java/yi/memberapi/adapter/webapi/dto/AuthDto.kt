package yi.memberapi.adapter.webapi.dto

data class LoginRequest(
    val username: String,
    val password: String,
    val rememberMe: Boolean = false
)

data class LoginResponse(
    val message: String = "로그인 성공",
    val expiresIn: Long
)

data class TokenResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long
)

data class RefreshResponse(
    val message: String = "토큰 갱신 성공",
    val expiresIn: Long
)

data class RegisterRequest(
    val username: String,
    val password: String,
    val name: String
)

data class RegisterResponse(
    val id: Long,
    val username: String,
    val name: String
)
