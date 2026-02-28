package yi.memberapi.application.required.favorite

import yi.memberapi.adapter.webapi.favorite.dto.request.AddFavoriteRequest
import yi.memberapi.adapter.webapi.favorite.dto.response.FavoriteResponse

interface FavoriteAdder {
    fun add(request: AddFavoriteRequest, memberId: Long): FavoriteResponse
}
