package yi.memberapi.adapter.webapi.dto.response

data class OpinionListResponse(
    val opinions: List<OpinionResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
