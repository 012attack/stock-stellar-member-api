package yi.memberapi.application.provided

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
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

    @Query("SELECT rc.targetId FROM ReadCheck rc WHERE rc.member.id = :memberId AND rc.targetType = :targetType")
    fun findTargetIdsByMemberIdAndTargetType(memberId: Long, targetType: ReadCheckTargetType): List<Int>
}
