package yi.memberapi.adapter.webapi.favorite

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.application.required.FavoriteRemover
import yi.memberapi.domain.favorite.FavoriteTargetType

@RestController
@RequestMapping("/api/favorites")
class RemoveFavoriteApi(
    private val favoriteRemover: FavoriteRemover
) {

    @DeleteMapping
    fun removeFavorite(
        @RequestParam targetType: FavoriteTargetType,
        @RequestParam targetId: Int
    ): ResponseEntity<Void> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        favoriteRemover.remove(targetType, targetId, memberId)
        return ResponseEntity.noContent().build()
    }
}
