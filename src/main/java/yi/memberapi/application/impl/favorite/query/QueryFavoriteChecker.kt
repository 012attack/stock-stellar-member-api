package yi.memberapi.application.impl.favorite.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.response.FavoriteCheckResponse
import yi.memberapi.application.provided.FavoriteRepository
import yi.memberapi.application.required.FavoriteChecker
import yi.memberapi.domain.favorite.FavoriteTargetType

@Service
@Transactional(readOnly = true)
class QueryFavoriteChecker(
    private val favoriteRepository: FavoriteRepository
) : FavoriteChecker {

    override fun check(targetType: FavoriteTargetType, targetId: Int, memberId: Long): FavoriteCheckResponse {
        val exists = favoriteRepository.existsByMemberIdAndTargetTypeAndTargetId(memberId, targetType, targetId)

        return FavoriteCheckResponse(
            targetType = targetType.name,
            targetId = targetId,
            favorited = exists
        )
    }
}
