package yi.memberapi.application.impl.stockdailyprice.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.stockdailyprice.dto.response.StockDailyPriceListResponse
import yi.memberapi.adapter.webapi.stockdailyprice.dto.response.StockDailyPriceResponse
import yi.memberapi.application.provided.StockDailyPriceRepository
import yi.memberapi.application.required.stock.StockFinder
import yi.memberapi.application.required.stockdailyprice.StockDailyPriceLister
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class QueryStockDailyPriceLister(
    private val stockDailyPriceRepository: StockDailyPriceRepository,
    private val stockFinder: StockFinder
) : StockDailyPriceLister {

    override fun list(stockId: Int, startDate: LocalDate?, endDate: LocalDate?, page: Int, size: Int): StockDailyPriceListResponse {
        val stock = stockFinder.findById(stockId)
            ?: throw IllegalArgumentException("Stock not found: $stockId")

        val pageable = PageRequest.of(page, size)

        val pricePage = if (startDate != null && endDate != null) {
            stockDailyPriceRepository.findByStockIdAndTradeDateBetweenOrderByTradeDateDesc(
                stockId, startDate, endDate, pageable
            )
        } else {
            stockDailyPriceRepository.findByStockIdOrderByTradeDateDesc(stockId, pageable)
        }

        val priceList = pricePage.content.map { price ->
            StockDailyPriceResponse(
                tradeDate = price.tradeDate,
                openPrice = price.openPrice,
                highPrice = price.highPrice,
                lowPrice = price.lowPrice,
                closePrice = price.closePrice,
                volume = price.volume,
                tradingValue = price.tradingValue,
                changeRate = price.changeRate
            )
        }

        return StockDailyPriceListResponse(
            prices = priceList,
            stockId = stock.id,
            stockCode = stock.stockCode,
            stockName = stock.stockName,
            page = pricePage.number,
            size = pricePage.size,
            totalElements = pricePage.totalElements,
            totalPages = pricePage.totalPages
        )
    }
}
