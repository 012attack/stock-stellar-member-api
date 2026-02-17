package yi.memberapi.adapter.webapi

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.dto.response.FavoriteListResponse
import yi.memberapi.application.required.FavoriteLister
import yi.memberapi.domain.favorite.FavoriteTargetType

@RestController
@RequestMapping("/api/favorites")
class GetFavoriteListApi(
    private val favoriteLister: FavoriteLister
) {

    @GetMapping
    fun getFavorites(
        @RequestParam targetType: FavoriteTargetType,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<FavoriteListResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        val response = favoriteLister.list(targetType, memberId, page, size)
        return ResponseEntity.ok(response)
    }
}
