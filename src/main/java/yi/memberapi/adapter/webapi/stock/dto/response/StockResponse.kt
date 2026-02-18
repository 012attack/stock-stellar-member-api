package yi.memberapi.adapter.webapi.stock.dto.response

import yi.memberapi.adapter.webapi.theme.dto.response.ThemeResponse

data class StockResponse(
    val id: Int,
    val stockCode: String,
    val stockName: String,
    val companySummary: String?,
    val themes: List<ThemeResponse> = emptyList()
)
