package yi.memberapi.application.impl.importance.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.provided.ImportanceRepository
import yi.memberapi.application.required.ImportanceRemover
import yi.memberapi.domain.importance.ImportanceTargetType

@Service
@Transactional
class CommandImportanceRemover(
    private val importanceRepository: ImportanceRepository
) : ImportanceRemover {

    override fun remove(targetType: ImportanceTargetType, targetId: Int, memberId: Long) {
        importanceRepository.deleteByMemberIdAndTargetTypeAndTargetId(memberId, targetType, targetId)
    }
}
