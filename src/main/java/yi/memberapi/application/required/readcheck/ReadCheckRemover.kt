package yi.memberapi.application.required.readcheck

import yi.memberapi.domain.readcheck.ReadCheckTargetType

interface ReadCheckRemover {
    fun remove(targetType: ReadCheckTargetType, targetId: Int, memberId: Long)
}
