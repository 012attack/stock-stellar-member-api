package yi.memberapi.application.required.favorite

import yi.memberapi.adapter.webapi.favorite.dto.response.FavoriteCheckResponse
import yi.memberapi.domain.favorite.FavoriteTargetType

interface FavoriteChecker {
    fun check(targetType: FavoriteTargetType, targetId: Int, memberId: Long): FavoriteCheckResponse
}
