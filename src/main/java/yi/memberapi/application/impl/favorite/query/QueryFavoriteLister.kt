package yi.memberapi.application.impl.favorite.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.favorite.dto.response.FavoriteListResponse
import yi.memberapi.adapter.webapi.favorite.dto.response.FavoriteResponse
import yi.memberapi.application.provided.FavoriteRepository
import yi.memberapi.application.required.FavoriteLister
import yi.memberapi.domain.favorite.FavoriteTargetType

@Service
@Transactional(readOnly = true)
class QueryFavoriteLister(
    private val favoriteRepository: FavoriteRepository
) : FavoriteLister {

    override fun list(targetType: FavoriteTargetType, memberId: Long, page: Int, size: Int): FavoriteListResponse {
        val pageable = PageRequest.of(page, size)
        val favoritePage = favoriteRepository.findByMemberIdAndTargetType(memberId, targetType, pageable)

        val favorites = favoritePage.content.map { favorite ->
            FavoriteResponse(
                id = favorite.id!!,
                targetType = favorite.targetType.name,
                targetId = favorite.targetId,
                createdAt = favorite.createdAt
            )
        }

        return FavoriteListResponse(
            favorites = favorites,
            page = favoritePage.number,
            size = favoritePage.size,
            totalElements = favoritePage.totalElements,
            totalPages = favoritePage.totalPages
        )
    }
}
