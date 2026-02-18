package yi.memberapi.application.impl.favorite.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.favorite.dto.request.AddFavoriteRequest
import yi.memberapi.adapter.webapi.favorite.dto.response.FavoriteResponse
import yi.memberapi.application.provided.FavoriteRepository
import yi.memberapi.application.required.FavoriteAdder
import yi.memberapi.application.required.MemberFinder
import yi.memberapi.domain.favorite.Favorite

@Service
@Transactional
class CommandFavoriteAdder(
    private val favoriteRepository: FavoriteRepository,
    private val memberFinder: MemberFinder
) : FavoriteAdder {

    override fun add(request: AddFavoriteRequest, memberId: Long): FavoriteResponse {
        val member = memberFinder.findById(memberId)

        val alreadyExists = favoriteRepository.existsByMemberIdAndTargetTypeAndTargetId(
            memberId, request.targetType, request.targetId
        )
        if (alreadyExists) {
            throw IllegalStateException("이미 즐겨찾기에 추가되어 있습니다.")
        }

        val favorite = Favorite(
            targetType = request.targetType,
            targetId = request.targetId,
            member = member
        )

        val saved = favoriteRepository.save(favorite)

        return FavoriteResponse(
            id = saved.id!!,
            targetType = saved.targetType.name,
            targetId = saved.targetId,
            createdAt = saved.createdAt
        )
    }
}
