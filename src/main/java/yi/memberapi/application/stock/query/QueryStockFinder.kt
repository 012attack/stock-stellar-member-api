package yi.memberapi.application.stock.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.response.StockResponse
import yi.memberapi.adapter.webapi.dto.response.ThemeResponse
import yi.memberapi.application.provided.StockRepository
import yi.memberapi.application.required.StockFinder

@Service
@Transactional(readOnly = true)
class QueryStockFinder(
    private val stockRepository: StockRepository
) : StockFinder {

    override fun findById(id: Int): StockResponse? {
        return stockRepository.findByIdWithThemes(id)?.let { stock ->
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
    }

    override fun findByCode(stockCode: String): StockResponse? {
        return stockRepository.findByStockCodeWithThemes(stockCode)?.let { stock ->
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
    }
}
