package yi.memberapi.adapter.webapi.dto.response

data class RefreshResponse(
    val message: String = "토큰 갱신 성공",
    val expiresIn: Long
)
