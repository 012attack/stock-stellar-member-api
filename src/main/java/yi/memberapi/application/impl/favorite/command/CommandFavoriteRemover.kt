package yi.memberapi.application.impl.favorite.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.provided.FavoriteRepository
import yi.memberapi.application.required.FavoriteRemover
import yi.memberapi.domain.favorite.FavoriteTargetType

@Service
@Transactional
class CommandFavoriteRemover(
    private val favoriteRepository: FavoriteRepository
) : FavoriteRemover {

    override fun remove(targetType: FavoriteTargetType, targetId: Int, memberId: Long) {
        favoriteRepository.deleteByMemberIdAndTargetTypeAndTargetId(memberId, targetType, targetId)
    }
}
