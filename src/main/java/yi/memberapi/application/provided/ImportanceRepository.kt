package yi.memberapi.application.provided

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.importance.Importance
import yi.memberapi.domain.importance.ImportanceTargetType
import java.math.BigDecimal
import java.util.Optional

interface ImportanceRepository : JpaRepository<Importance, Int> {

    fun findByMemberIdAndTargetTypeAndTargetId(
        memberId: Long,
        targetType: ImportanceTargetType,
        targetId: Int
    ): Optional<Importance>

    fun existsByMemberIdAndTargetTypeAndTargetId(
        memberId: Long,
        targetType: ImportanceTargetType,
        targetId: Int
    ): Boolean

    fun deleteByMemberIdAndTargetTypeAndTargetId(
        memberId: Long,
        targetType: ImportanceTargetType,
        targetId: Int
    )

    @Query("SELECT i.targetId FROM Importance i WHERE i.member.id = :memberId AND i.targetType = :targetType AND i.score >= :minScore")
    fun findTargetIdsByMemberIdAndTargetTypeAndMinScore(
        memberId: Long,
        targetType: ImportanceTargetType,
        minScore: BigDecimal
    ): List<Int>
}
