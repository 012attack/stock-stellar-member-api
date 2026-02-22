package yi.memberapi.application.impl.importance.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.importance.dto.request.SetImportanceRequest
import yi.memberapi.adapter.webapi.importance.dto.response.ImportanceResponse
import yi.memberapi.application.provided.ImportanceRepository
import yi.memberapi.application.required.ImportanceSetter
import yi.memberapi.application.required.MemberFinder
import yi.memberapi.domain.importance.Importance

@Service
@Transactional
class CommandImportanceSetter(
    private val importanceRepository: ImportanceRepository,
    private val memberFinder: MemberFinder
) : ImportanceSetter {

    override fun set(request: SetImportanceRequest, memberId: Long): ImportanceResponse {
        val member = memberFinder.findById(memberId)

        val existing = importanceRepository.findByMemberIdAndTargetTypeAndTargetId(
            memberId, request.targetType, request.targetId
        )

        val saved = if (existing.isPresent) {
            val importance = existing.get()
            importance.score = request.score
            importanceRepository.save(importance)
        } else {
            val importance = Importance(
                targetType = request.targetType,
                targetId = request.targetId,
                score = request.score,
                member = member
            )
            importanceRepository.save(importance)
        }

        return ImportanceResponse(
            id = saved.id!!,
            targetType = saved.targetType.name,
            targetId = saved.targetId,
            score = saved.score,
            createdAt = saved.createdAt,
            updatedAt = saved.updatedAt
        )
    }
}
