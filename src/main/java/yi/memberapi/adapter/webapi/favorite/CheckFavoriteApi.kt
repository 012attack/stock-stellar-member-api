package yi.memberapi.adapter.webapi.favorite

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.favorite.dto.response.FavoriteCheckResponse
import yi.memberapi.application.required.FavoriteChecker
import yi.memberapi.domain.favorite.FavoriteTargetType

@RestController
@RequestMapping("/api/favorites")
class CheckFavoriteApi(
    private val favoriteChecker: FavoriteChecker
) {

    @GetMapping("/check")
    fun checkFavorite(
        @RequestParam targetType: FavoriteTargetType,
        @RequestParam targetId: Int
    ): ResponseEntity<FavoriteCheckResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        val response = favoriteChecker.check(targetType, targetId, memberId)
        return ResponseEntity.ok(response)
    }
}
