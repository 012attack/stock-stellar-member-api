package yi.memberapi.adapter.webapi.record.dto.response

import yi.memberapi.adapter.webapi.stock.dto.response.StockResponse
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeResponse
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
