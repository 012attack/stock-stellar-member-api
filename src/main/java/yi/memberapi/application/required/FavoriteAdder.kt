package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.request.AddFavoriteRequest
import yi.memberapi.adapter.webapi.dto.response.FavoriteResponse

interface FavoriteAdder {
    fun add(request: AddFavoriteRequest, memberId: Long): FavoriteResponse
}
