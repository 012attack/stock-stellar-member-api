package yi.memberapi.adapter.webapi.theme.dto.response

import yi.memberapi.adapter.webapi.news.dto.response.NewsResponse
import yi.memberapi.adapter.webapi.stock.dto.response.StockResponse

data class ThemeDetailResponse(
    val id: Int,
    val themeName: String,
    val news: List<NewsResponse> = emptyList(),
    val stocks: List<StockResponse> = emptyList()
)
