package yi.memberapi.application.stockgroup.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.response.StockGroupListResponse
import yi.memberapi.adapter.webapi.dto.response.StockGroupResponse
import yi.memberapi.application.provided.StockGroupRepository
import yi.memberapi.application.required.StockGroupLister

@Service
@Transactional(readOnly = true)
class QueryStockGroupLister(
    private val stockGroupRepository: StockGroupRepository
) : StockGroupLister {

    override fun list(page: Int, size: Int, memberId: Long): StockGroupListResponse {
        val pageable = PageRequest.of(page, size)
        val stockGroupPage = stockGroupRepository.findByMemberId(memberId, pageable)

        val stockGroupList = stockGroupPage.content.map { stockGroup ->
            StockGroupResponse(
                id = stockGroup.id!!,
                title = stockGroup.title,
                description = stockGroup.description,
                stockCount = stockGroup.stocks.size,
                createdAt = stockGroup.createdAt,
                updatedAt = stockGroup.updatedAt
            )
        }

        return StockGroupListResponse(
            stockGroups = stockGroupList,
            page = stockGroupPage.number,
            size = stockGroupPage.size,
            totalElements = stockGroupPage.totalElements,
            totalPages = stockGroupPage.totalPages
        )
    }
}
