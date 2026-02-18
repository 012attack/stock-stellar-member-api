package yi.memberapi.adapter.webapi.auth.dto.response

data class MemberListResponse(
    val members: List<MemberResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
