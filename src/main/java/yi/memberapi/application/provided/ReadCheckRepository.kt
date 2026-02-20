package yi.memberapi.application.provided

import org.springframework.data.jpa.repository.JpaRepository
import yi.memberapi.domain.readcheck.ReadCheck
import yi.memberapi.domain.readcheck.ReadCheckTargetType

interface ReadCheckRepository : JpaRepository<ReadCheck, Int> {

    fun existsByMemberIdAndTargetTypeAndTargetId(
        memberId: Long,
        targetType: ReadCheckTargetType,
        targetId: Int
    ): Boolean

    fun deleteByMemberIdAndTargetTypeAndTargetId(
        memberId: Long,
        targetType: ReadCheckTargetType,
        targetId: Int
    )
}
