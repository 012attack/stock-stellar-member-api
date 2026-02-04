package yi.memberapi.adapter.webapi.dto.response

import java.time.LocalDate
import java.time.LocalDateTime

data class DailyTop30RecordResponse(
    val id: Int,
    val recordDate: LocalDate,
    val rank: Int,
    val changeRate: String?,
    val description: String?,
    val createdAt: LocalDateTime?,
    val stock: StockResponse?,
    val themes: List<ThemeResponse>
)
