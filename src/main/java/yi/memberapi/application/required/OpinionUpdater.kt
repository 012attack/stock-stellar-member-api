package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.request.UpdateOpinionRequest
import yi.memberapi.adapter.webapi.dto.response.OpinionResponse

interface OpinionUpdater {
    fun update(opinionId: Int, request: UpdateOpinionRequest, memberId: Long): OpinionResponse
}
