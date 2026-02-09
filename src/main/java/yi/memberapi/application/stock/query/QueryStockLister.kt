package yi.memberapi.application.stock.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.response.StockListResponse
import yi.memberapi.adapter.webapi.dto.response.StockResponse
import yi.memberapi.application.provided.StockRepository
import yi.memberapi.application.required.StockLister

@Service
@Transactional(readOnly = true)
class QueryStockLister(
    private val stockRepository: StockRepository
) : StockLister {

    override fun list(page: Int, size: Int, stockName: String?, stockCode: String?): StockListResponse {
        val pageable = PageRequest.of(page, size)
        val stockPage = stockRepository.findWithFilters(stockName, stockCode, pageable)

        val stockList = stockPage.content.map { stock ->
            StockResponse(
                id = stock.id!!,
                stockCode = stock.stockCode,
                stockName = stock.stockName,
                companySummary = stock.companySummary
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
