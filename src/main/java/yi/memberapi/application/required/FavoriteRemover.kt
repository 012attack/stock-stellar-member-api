package yi.memberapi.application.required

import yi.memberapi.domain.favorite.FavoriteTargetType

interface FavoriteRemover {
    fun remove(targetType: FavoriteTargetType, targetId: Int, memberId: Long)
}
