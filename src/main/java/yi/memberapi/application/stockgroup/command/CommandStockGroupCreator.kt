package yi.memberapi.application.stockgroup.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.request.CreateStockGroupRequest
import yi.memberapi.adapter.webapi.dto.response.StockGroupResponse
import yi.memberapi.application.provided.MemberRepository
import yi.memberapi.application.provided.StockGroupRepository
import yi.memberapi.application.required.StockGroupCreator
import yi.memberapi.domain.stockgroup.StockGroup

@Service
@Transactional
class CommandStockGroupCreator(
    private val stockGroupRepository: StockGroupRepository,
    private val memberRepository: MemberRepository
) : StockGroupCreator {

    override fun create(request: CreateStockGroupRequest, memberId: Long): StockGroupResponse {
        val member = memberRepository.findById(memberId)
            .orElseThrow { IllegalArgumentException("Member not found: $memberId") }

        val stockGroup = StockGroup(
            title = request.title,
            description = request.description,
            member = member
        )

        val saved = stockGroupRepository.save(stockGroup)

        return StockGroupResponse(
            id = saved.id!!,
            title = saved.title,
            description = saved.description,
            stockCount = 0,
            createdAt = saved.createdAt,
            updatedAt = saved.updatedAt
        )
    }
}
