package yi.memberapi.application.impl.importance.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.importance.dto.response.ImportanceResponse
import yi.memberapi.application.provided.ImportanceRepository
import yi.memberapi.application.required.ImportanceGetter
import yi.memberapi.domain.importance.ImportanceTargetType

@Service
@Transactional(readOnly = true)
class QueryImportanceGetter(
    private val importanceRepository: ImportanceRepository
) : ImportanceGetter {

    override fun get(targetType: ImportanceTargetType, targetId: Int, memberId: Long): ImportanceResponse {
        val importance = importanceRepository.findByMemberIdAndTargetTypeAndTargetId(
            memberId, targetType, targetId
        ).orElseThrow { IllegalArgumentException("별점이 존재하지 않습니다.") }

        return ImportanceResponse(
            id = importance.id!!,
            targetType = importance.targetType.name,
            targetId = importance.targetId,
            score = importance.score,
            createdAt = importance.createdAt,
            updatedAt = importance.updatedAt
        )
    }
}
