package yi.memberapi.application.required.opinion

import yi.memberapi.adapter.webapi.opinion.dto.response.OpinionListResponse
import yi.memberapi.domain.opinion.TargetType
import yi.memberapi.domain.readcheck.ReadFilter

interface OpinionLister {
    fun listByTarget(targetType: TargetType, targetId: Int, page: Int, size: Int, favoriteOnly: Boolean = false, memberId: Long? = null, readFilter: ReadFilter? = null): OpinionListResponse
    fun listByTargetType(targetType: TargetType, page: Int, size: Int, favoriteOnly: Boolean = false, memberId: Long? = null, readFilter: ReadFilter? = null): OpinionListResponse
}
