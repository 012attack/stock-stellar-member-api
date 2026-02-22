package yi.memberapi.adapter.webapi.schedule.dto.response

import java.time.LocalDate
import java.time.LocalDateTime

data class StockMarketScheduleDetailResponse(
    val id: Int,
    val title: String,
    val content: String?,
    val scheduleDate: LocalDate,
    val memberName: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
