package yi.memberapi.adapter.webapi.dto.response

data class ThemeDetailResponse(
    val id: Int,
    val themeName: String,
    val news: List<NewsResponse> = emptyList(),
    val stocks: List<StockResponse> = emptyList()
)
