package yi.memberapi.adapter.webapi.favorite.dto.response

import java.time.LocalDateTime

data class FavoriteResponse(
    val id: Int,
    val targetType: String,
    val targetId: Int,
    val createdAt: LocalDateTime?
)
