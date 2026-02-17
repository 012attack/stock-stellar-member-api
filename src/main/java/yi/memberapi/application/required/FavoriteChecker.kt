package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.response.FavoriteCheckResponse
import yi.memberapi.domain.favorite.FavoriteTargetType

interface FavoriteChecker {
    fun check(targetType: FavoriteTargetType, targetId: Int, memberId: Long): FavoriteCheckResponse
}
