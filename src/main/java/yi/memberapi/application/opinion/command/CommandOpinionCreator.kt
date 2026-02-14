package yi.memberapi.application.opinion.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.request.CreateOpinionRequest
import yi.memberapi.adapter.webapi.dto.response.OpinionResponse
import yi.memberapi.application.provided.MemberRepository
import yi.memberapi.application.provided.OpinionRepository
import yi.memberapi.application.required.OpinionCreator
import yi.memberapi.domain.opinion.Opinion
import yi.memberapi.domain.opinion.TargetType

@Service
@Transactional
class CommandOpinionCreator(
    private val opinionRepository: OpinionRepository,
    private val memberRepository: MemberRepository
) : OpinionCreator {

    override fun create(request: CreateOpinionRequest, targetType: TargetType, targetId: Int, memberId: Long): OpinionResponse {
        val member = memberRepository.findById(memberId)
            .orElseThrow { IllegalArgumentException("Member not found: $memberId") }

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
