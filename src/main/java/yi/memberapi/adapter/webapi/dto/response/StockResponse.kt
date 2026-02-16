package yi.memberapi.adapter.webapi.dto.response

data class StockResponse(
    val id: Int,
    val stockCode: String,
    val stockName: String,
    val companySummary: String?,
    val themes: List<ThemeResponse> = emptyList()
)
