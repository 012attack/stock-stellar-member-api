package yi.memberapi.application.required

import yi.memberapi.domain.importance.ImportanceTargetType

interface ImportanceRemover {
    fun remove(targetType: ImportanceTargetType, targetId: Int, memberId: Long)
}
