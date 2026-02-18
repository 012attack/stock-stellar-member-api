package yi.memberapi.adapter.webapi.stockgroup.dto.response

import yi.memberapi.adapter.webapi.stock.dto.response.StockResponse
import java.time.LocalDateTime

data class StockGroupDetailResponse(
    val id: Int,
    val title: String,
    val description: String?,
    val stocks: List<StockResponse>,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
