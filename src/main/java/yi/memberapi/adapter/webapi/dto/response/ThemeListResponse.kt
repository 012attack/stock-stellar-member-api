package yi.memberapi.adapter.webapi.dto.response

data class ThemeListResponse(
    val themes: List<ThemeResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
