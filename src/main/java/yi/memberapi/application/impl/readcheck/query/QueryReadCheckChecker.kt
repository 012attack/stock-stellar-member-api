package yi.memberapi.application.impl.readcheck.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.readcheck.dto.response.ReadCheckCheckResponse
import yi.memberapi.application.provided.ReadCheckRepository
import yi.memberapi.application.required.ReadCheckChecker
import yi.memberapi.domain.readcheck.ReadCheckTargetType

@Service
@Transactional(readOnly = true)
class QueryReadCheckChecker(
    private val readCheckRepository: ReadCheckRepository
) : ReadCheckChecker {

    override fun check(targetType: ReadCheckTargetType, targetId: Int, memberId: Long): ReadCheckCheckResponse {
        val exists = readCheckRepository.existsByMemberIdAndTargetTypeAndTargetId(memberId, targetType, targetId)

        return ReadCheckCheckResponse(
            targetType = targetType.name,
            targetId = targetId,
            checked = exists
        )
    }
}
