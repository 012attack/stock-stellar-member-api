package yi.memberapi.application.impl.stock.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.stock.dto.response.StockListResponse
import yi.memberapi.adapter.webapi.stock.dto.response.StockResponse
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeResponse
import yi.memberapi.application.provided.FavoriteRepository
import yi.memberapi.application.provided.StockRepository
import yi.memberapi.application.required.StockLister
import yi.memberapi.domain.favorite.FavoriteTargetType

@Service
@Transactional(readOnly = true)
class QueryStockLister(
    private val stockRepository: StockRepository,
    private val favoriteRepository: FavoriteRepository
) : StockLister {

    override fun list(page: Int, size: Int, stockName: String?, stockCode: String?, themeName: String?, favoriteOnly: Boolean, memberId: Long?): StockListResponse {
        val pageable = PageRequest.of(page, size)

        val stockPage = if (favoriteOnly && memberId != null) {
            val favoriteIds = favoriteRepository.findTargetIdsByMemberIdAndTargetType(memberId, FavoriteTargetType.STOCK)
            if (favoriteIds.isEmpty()) return StockListResponse(stocks = emptyList(), page = page, size = size, totalElements = 0, totalPages = 0)
            stockRepository.findWithFiltersByFavoriteIds(stockName, stockCode, themeName, favoriteIds, pageable)
        } else {
            stockRepository.findWithFilters(stockName, stockCode, themeName, pageable)
        }

        val stockList = stockPage.content.map { stock ->
            StockResponse(
                id = stock.id!!,
                stockCode = stock.stockCode,
                stockName = stock.stockName,
                companySummary = stock.companySummary,
                themes = stock.themes.map { theme ->
                    ThemeResponse(
                        id = theme.id!!,
                        themeName = theme.themeName
                    )
                }
            )
        }

        return StockListResponse(
            stocks = stockList,
            page = stockPage.number,
            size = stockPage.size,
            totalElements = stockPage.totalElements,
            totalPages = stockPage.totalPages
        )
    }
}
