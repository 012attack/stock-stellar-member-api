package yi.memberapi.adapter.webapi.auth.dto.request

data class LoginRequest(
    val username: String,
    val password: String,
    val rememberMe: Boolean = false
)
