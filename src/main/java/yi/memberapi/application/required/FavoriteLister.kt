package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.response.FavoriteListResponse
import yi.memberapi.domain.favorite.FavoriteTargetType

interface FavoriteLister {
    fun list(targetType: FavoriteTargetType, memberId: Long, page: Int, size: Int): FavoriteListResponse
}
