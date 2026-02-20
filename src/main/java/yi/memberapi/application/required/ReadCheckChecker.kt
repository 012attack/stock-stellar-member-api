package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.readcheck.dto.response.ReadCheckCheckResponse
import yi.memberapi.domain.readcheck.ReadCheckTargetType

interface ReadCheckChecker {
    fun check(targetType: ReadCheckTargetType, targetId: Int, memberId: Long): ReadCheckCheckResponse
}
