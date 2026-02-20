package yi.memberapi.application.impl.stockgroup.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.stockgroup.dto.response.StockGroupListResponse
import yi.memberapi.adapter.webapi.stockgroup.dto.response.StockGroupResponse
import yi.memberapi.application.provided.FavoriteRepository
import yi.memberapi.application.provided.StockGroupRepository
import yi.memberapi.application.required.StockGroupLister
import yi.memberapi.domain.favorite.FavoriteTargetType

@Service
@Transactional(readOnly = true)
class QueryStockGroupLister(
    private val stockGroupRepository: StockGroupRepository,
    private val favoriteRepository: FavoriteRepository
) : StockGroupLister {

    override fun list(page: Int, size: Int, memberId: Long, favoriteOnly: Boolean): StockGroupListResponse {
        val pageable = PageRequest.of(page, size)

        val stockGroupPage = if (favoriteOnly) {
            val favoriteIds = favoriteRepository.findTargetIdsByMemberIdAndTargetType(memberId, FavoriteTargetType.STOCK_GROUP)
            if (favoriteIds.isEmpty()) return StockGroupListResponse(stockGroups = emptyList(), page = page, size = size, totalElements = 0, totalPages = 0)
            stockGroupRepository.findByMemberIdAndFavoriteIds(memberId, favoriteIds, pageable)
        } else {
            stockGroupRepository.findByMemberId(memberId, pageable)
        }

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
