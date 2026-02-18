package yi.memberapi.application.impl.stockgroup.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.stockgroup.dto.response.StockGroupDetailResponse
import yi.memberapi.adapter.webapi.stock.dto.response.StockResponse
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeResponse
import yi.memberapi.application.provided.StockGroupRepository
import yi.memberapi.application.required.StockGroupFinder
import yi.memberapi.domain.stockgroup.StockGroup

@Service
@Transactional(readOnly = true)
class QueryStockGroupFinder(
    private val stockGroupRepository: StockGroupRepository
) : StockGroupFinder {

    override fun findEntityByIdWithMember(id: Int): StockGroup {
        return stockGroupRepository.findByIdWithMember(id)
            ?: throw IllegalArgumentException("StockGroup not found: $id")
    }

    override fun findEntityByIdWithStocks(id: Int): StockGroup {
        return stockGroupRepository.findByIdWithStocks(id)
            ?: throw IllegalArgumentException("StockGroup not found: $id")
    }

    override fun find(id: Int, memberId: Long): StockGroupDetailResponse {
        val stockGroup = stockGroupRepository.findByIdWithStocks(id)
            ?: throw IllegalArgumentException("StockGroup not found: $id")

        if (stockGroup.member?.id != memberId) {
            throw IllegalArgumentException("Not authorized to view this stock group")
        }

        return StockGroupDetailResponse(
            id = stockGroup.id!!,
            title = stockGroup.title,
            description = stockGroup.description,
            stocks = stockGroup.stocks.map { stock ->
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
            },
            createdAt = stockGroup.createdAt,
            updatedAt = stockGroup.updatedAt
        )
    }
}
