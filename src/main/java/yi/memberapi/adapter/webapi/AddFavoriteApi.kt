package yi.memberapi.adapter.webapi

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.dto.request.AddFavoriteRequest
import yi.memberapi.adapter.webapi.dto.response.FavoriteResponse
import yi.memberapi.application.required.FavoriteAdder
import java.net.URI

@RestController
@RequestMapping("/api/favorites")
class AddFavoriteApi(
    private val favoriteAdder: FavoriteAdder
) {

    @PostMapping
    fun addFavorite(@RequestBody request: AddFavoriteRequest): ResponseEntity<FavoriteResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        val response = favoriteAdder.add(request, memberId)
        return ResponseEntity.created(URI.create("/api/favorites/${response.id}")).body(response)
    }
}
