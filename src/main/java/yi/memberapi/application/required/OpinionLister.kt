package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.opinion.dto.response.OpinionListResponse
import yi.memberapi.domain.opinion.TargetType

interface OpinionLister {
    fun listByTarget(targetType: TargetType, targetId: Int, page: Int, size: Int): OpinionListResponse
    fun listByTargetType(targetType: TargetType, page: Int, size: Int): OpinionListResponse
}
