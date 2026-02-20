package yi.memberapi.application.impl.readcheck.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.provided.ReadCheckRepository
import yi.memberapi.application.required.ReadCheckRemover
import yi.memberapi.domain.readcheck.ReadCheckTargetType

@Service
@Transactional
class CommandReadCheckRemover(
    private val readCheckRepository: ReadCheckRepository
) : ReadCheckRemover {

    override fun remove(targetType: ReadCheckTargetType, targetId: Int, memberId: Long) {
        readCheckRepository.deleteByMemberIdAndTargetTypeAndTargetId(memberId, targetType, targetId)
    }
}
