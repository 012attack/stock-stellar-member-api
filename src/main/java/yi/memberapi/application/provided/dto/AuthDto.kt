package yi.memberapi.application.provided.dto

data class LoginRequest(
    val username: String,
    val password: String
)

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long
)

data class RefreshTokenRequest(
    val refreshToken: String
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
