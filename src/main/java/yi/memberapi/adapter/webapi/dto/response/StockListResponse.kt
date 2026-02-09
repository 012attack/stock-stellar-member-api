package yi.memberapi.adapter.webapi.dto.response

data class StockListResponse(
    val stocks: List<StockResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
