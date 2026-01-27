package yi.memberapi.adapter.webapi.dto.response

data class TokenResponse(
    val accessToken: String,
    val tokenType: String = "Bearer",
    val expiresIn: Long
)
