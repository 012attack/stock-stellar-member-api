package yi.memberapi.adapter.webapi.stockdailyprice.dto.response

import java.math.BigDecimal
import java.time.LocalDate

data class StockDailyPriceResponse(
    val tradeDate: LocalDate,
    val openPrice: Long?,
    val highPrice: Long?,
    val lowPrice: Long?,
    val closePrice: Long?,
    val volume: Long?,
    val tradingValue: Long?,
    val changeRate: BigDecimal?
)
