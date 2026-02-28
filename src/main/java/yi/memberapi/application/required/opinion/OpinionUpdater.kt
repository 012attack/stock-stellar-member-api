package yi.memberapi.application.required.opinion

import yi.memberapi.adapter.webapi.opinion.dto.request.UpdateOpinionRequest
import yi.memberapi.adapter.webapi.opinion.dto.response.OpinionResponse

interface OpinionUpdater {
    fun update(opinionId: Int, request: UpdateOpinionRequest, memberId: Long): OpinionResponse
}
