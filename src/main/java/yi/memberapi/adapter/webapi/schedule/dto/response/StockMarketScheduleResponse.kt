package yi.memberapi.adapter.webapi.schedule.dto.response

import java.time.LocalDate
import java.time.LocalDateTime

data class StockMarketScheduleResponse(
    val id: Int,
    val title: String,
    val content: String?,
    val scheduleDate: LocalDate,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
