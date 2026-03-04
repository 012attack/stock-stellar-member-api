package yi.memberapi.application.required.stockdailyprice

import yi.memberapi.adapter.webapi.stockdailyprice.dto.response.StockDailyPriceListResponse
import java.time.LocalDate

interface StockDailyPriceLister {
    fun list(stockId: Int, startDate: LocalDate?, endDate: LocalDate?, page: Int, size: Int): StockDailyPriceListResponse
}
