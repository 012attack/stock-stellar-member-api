package yi.memberapi.application.impl.opinion.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.opinion.dto.request.CreateOpinionRequest
import yi.memberapi.adapter.webapi.opinion.dto.response.OpinionResponse
import yi.memberapi.application.provided.OpinionRepository
import yi.memberapi.application.required.MemberFinder
import yi.memberapi.application.required.OpinionCreator
import yi.memberapi.domain.opinion.Opinion
import yi.memberapi.domain.opinion.TargetType

@Service
@Transactional
class CommandOpinionCreator(
    private val opinionRepository: OpinionRepository,
    private val memberFinder: MemberFinder
) : OpinionCreator {

    override fun create(request: CreateOpinionRequest, targetType: TargetType, targetId: Int, memberId: Long): OpinionResponse {
        val member = memberFinder.findById(memberId)

        val opinion = Opinion(
            title = request.title,
            content = request.content,
            targetType = targetType,
            targetId = targetId,
            member = member
        )

        val saved = opinionRepository.save(opinion)

        return OpinionResponse(
            id = saved.id!!,
            title = saved.title,
            content = saved.content,
            memberName = member.name ?: "",
            createdAt = saved.createdAt,
            targetType = saved.targetType.name,
            targetId = saved.targetId
        )
    }
}
