package yi.memberapi.application.impl.stockgroup.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.stockgroup.dto.request.CreateStockGroupRequest
import yi.memberapi.adapter.webapi.stockgroup.dto.response.StockGroupResponse
import yi.memberapi.application.provided.StockGroupRepository
import yi.memberapi.application.required.MemberFinder
import yi.memberapi.application.required.StockGroupCreator
import yi.memberapi.domain.stockgroup.StockGroup

@Service
@Transactional
class CommandStockGroupCreator(
    private val stockGroupRepository: StockGroupRepository,
    private val memberFinder: MemberFinder
) : StockGroupCreator {

    override fun create(request: CreateStockGroupRequest, memberId: Long): StockGroupResponse {
        val member = memberFinder.findById(memberId)

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
