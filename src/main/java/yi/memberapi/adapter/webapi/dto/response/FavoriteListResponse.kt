package yi.memberapi.adapter.webapi.dto.response

data class FavoriteListResponse(
    val favorites: List<FavoriteResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
