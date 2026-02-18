package yi.memberapi.adapter.webapi.stockgroup.dto.response

import java.time.LocalDateTime

data class StockGroupResponse(
    val id: Int,
    val title: String,
    val description: String?,
    val stockCount: Int,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
