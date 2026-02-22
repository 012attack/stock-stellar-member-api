package yi.memberapi.application.provided

import org.springframework.data.jpa.repository.JpaRepository
import yi.memberapi.domain.importance.Importance
import yi.memberapi.domain.importance.ImportanceTargetType
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
}
