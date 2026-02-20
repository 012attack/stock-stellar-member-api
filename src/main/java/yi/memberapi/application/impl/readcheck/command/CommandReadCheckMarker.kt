package yi.memberapi.application.impl.readcheck.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.readcheck.dto.request.MarkReadCheckRequest
import yi.memberapi.adapter.webapi.readcheck.dto.response.ReadCheckResponse
import yi.memberapi.application.provided.ReadCheckRepository
import yi.memberapi.application.required.MemberFinder
import yi.memberapi.application.required.ReadCheckMarker
import yi.memberapi.domain.readcheck.ReadCheck

@Service
@Transactional
class CommandReadCheckMarker(
    private val readCheckRepository: ReadCheckRepository,
    private val memberFinder: MemberFinder
) : ReadCheckMarker {

    override fun mark(request: MarkReadCheckRequest, memberId: Long): ReadCheckResponse {
        val member = memberFinder.findById(memberId)

        val alreadyExists = readCheckRepository.existsByMemberIdAndTargetTypeAndTargetId(
            memberId, request.targetType, request.targetId
        )
        if (alreadyExists) {
            throw IllegalStateException("이미 조회 확인된 항목입니다.")
        }

        val readCheck = ReadCheck(
            targetType = request.targetType,
            targetId = request.targetId,
            member = member
        )

        val saved = readCheckRepository.save(readCheck)

        return ReadCheckResponse(
            id = saved.id!!,
            targetType = saved.targetType.name,
            targetId = saved.targetId,
            createdAt = saved.createdAt
        )
    }
}
