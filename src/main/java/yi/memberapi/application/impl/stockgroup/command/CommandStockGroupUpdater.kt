package yi.memberapi.application.impl.stockgroup.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.request.UpdateStockGroupRequest
import yi.memberapi.adapter.webapi.dto.response.StockGroupResponse
import yi.memberapi.application.required.StockGroupFinder
import yi.memberapi.application.required.StockGroupUpdater

@Service
@Transactional
class CommandStockGroupUpdater(
    private val stockGroupFinder: StockGroupFinder
) : StockGroupUpdater {

    override fun update(id: Int, request: UpdateStockGroupRequest, memberId: Long): StockGroupResponse {
        val stockGroup = stockGroupFinder.findEntityByIdWithMember(id)

        if (stockGroup.member?.id != memberId) {
            throw IllegalArgumentException("Not authorized to update this stock group")
        }

        stockGroup.title = request.title
        stockGroup.description = request.description

        return StockGroupResponse(
            id = stockGroup.id!!,
            title = stockGroup.title,
            description = stockGroup.description,
            stockCount = stockGroup.stocks.size,
            createdAt = stockGroup.createdAt,
            updatedAt = stockGroup.updatedAt
        )
    }
}
