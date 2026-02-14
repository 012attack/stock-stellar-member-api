package yi.memberapi.application.opinion.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.response.OpinionListResponse
import yi.memberapi.adapter.webapi.dto.response.OpinionResponse
import yi.memberapi.application.provided.OpinionRepository
import yi.memberapi.application.required.OpinionLister
import yi.memberapi.domain.opinion.TargetType

@Service
@Transactional(readOnly = true)
class QueryOpinionLister(
    private val opinionRepository: OpinionRepository
) : OpinionLister {

    override fun listByTarget(targetType: TargetType, targetId: Int, page: Int, size: Int): OpinionListResponse {
        val pageable = PageRequest.of(page, size)
        val opinionPage = opinionRepository.findByTargetTypeAndTargetId(targetType, targetId, pageable)

        val opinions = opinionPage.content.map { opinion ->
            OpinionResponse(
                id = opinion.id!!,
                title = opinion.title,
                content = opinion.content,
                memberName = opinion.member?.name ?: "",
                createdAt = opinion.createdAt,
                targetType = opinion.targetType.name,
                targetId = opinion.targetId
            )
        }

        return OpinionListResponse(
            opinions = opinions,
            page = opinionPage.number,
            size = opinionPage.size,
            totalElements = opinionPage.totalElements,
            totalPages = opinionPage.totalPages
        )
    }

    override fun listByTargetType(targetType: TargetType, page: Int, size: Int): OpinionListResponse {
        val pageable = PageRequest.of(page, size)
        val opinionPage = opinionRepository.findByTargetType(targetType, pageable)

        val opinions = opinionPage.content.map { opinion ->
            OpinionResponse(
                id = opinion.id!!,
                title = opinion.title,
                content = opinion.content,
                memberName = opinion.member?.name ?: "",
                createdAt = opinion.createdAt,
                targetType = opinion.targetType.name,
                targetId = opinion.targetId
            )
        }

        return OpinionListResponse(
            opinions = opinions,
            page = opinionPage.number,
            size = opinionPage.size,
            totalElements = opinionPage.totalElements,
            totalPages = opinionPage.totalPages
        )
    }
}
