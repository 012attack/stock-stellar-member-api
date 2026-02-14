package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.request.CreateOpinionRequest
import yi.memberapi.adapter.webapi.dto.response.OpinionResponse
import yi.memberapi.domain.opinion.TargetType

interface OpinionCreator {
    fun create(request: CreateOpinionRequest, targetType: TargetType, targetId: Int, memberId: Long): OpinionResponse
}
