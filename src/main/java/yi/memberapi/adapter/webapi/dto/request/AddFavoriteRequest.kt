package yi.memberapi.adapter.webapi.dto.request

import yi.memberapi.domain.favorite.FavoriteTargetType

data class AddFavoriteRequest(
    val targetType: FavoriteTargetType,
    val targetId: Int
)
