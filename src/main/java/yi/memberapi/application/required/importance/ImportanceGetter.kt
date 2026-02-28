package yi.memberapi.application.required.importance

import yi.memberapi.adapter.webapi.importance.dto.response.ImportanceResponse
import yi.memberapi.domain.importance.ImportanceTargetType

interface ImportanceGetter {
    fun get(targetType: ImportanceTargetType, targetId: Int, memberId: Long): ImportanceResponse?
}
