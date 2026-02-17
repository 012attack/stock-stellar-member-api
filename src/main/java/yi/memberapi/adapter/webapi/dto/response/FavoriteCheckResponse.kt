package yi.memberapi.adapter.webapi.dto.response

data class FavoriteCheckResponse(
    val targetType: String,
    val targetId: Int,
    val favorited: Boolean
)
