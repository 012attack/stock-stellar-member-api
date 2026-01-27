package yi.memberapi.adapter.webapi.dto.response

data class LoginResponse(
    val message: String = "로그인 성공",
    val expiresIn: Long
)
