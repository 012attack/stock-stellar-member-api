package yi.memberapi.adapter.webapi.stockdailyprice.dto.response

data class StockDailyPriceListResponse(
    val prices: List<StockDailyPriceResponse>,
    val stockId: Int,
    val stockCode: String,
    val stockName: String,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
