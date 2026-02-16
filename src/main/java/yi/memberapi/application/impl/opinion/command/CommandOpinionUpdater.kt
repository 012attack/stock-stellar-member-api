package yi.memberapi.application.impl.opinion.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.request.UpdateOpinionRequest
import yi.memberapi.adapter.webapi.dto.response.OpinionResponse
import yi.memberapi.application.required.OpinionFinder
import yi.memberapi.application.required.OpinionUpdater

@Service
@Transactional
class CommandOpinionUpdater(
    private val opinionFinder: OpinionFinder
) : OpinionUpdater {

    override fun update(opinionId: Int, request: UpdateOpinionRequest, memberId: Long): OpinionResponse {
        val opinion = opinionFinder.findEntityById(opinionId)

        if (opinion.member?.id != memberId) {
            throw IllegalArgumentException("본인의 의견만 수정할 수 있습니다.")
        }

        opinion.title = request.title
        opinion.content = request.content

        return OpinionResponse(
            id = opinion.id!!,
            title = opinion.title,
            content = opinion.content,
            memberName = opinion.member?.name ?: "",
            createdAt = opinion.createdAt,
            targetType = opinion.targetType.name,
            targetId = opinion.targetId
        )
    }
}
