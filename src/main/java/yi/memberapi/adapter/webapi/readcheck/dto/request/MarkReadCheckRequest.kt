package yi.memberapi.adapter.webapi.readcheck.dto.request

import yi.memberapi.domain.readcheck.ReadCheckTargetType

data class MarkReadCheckRequest(
    val targetType: ReadCheckTargetType,
    val targetId: Int
)
