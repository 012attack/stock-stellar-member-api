package yi.memberapi.adapter.webapi.schedule.dto.request

import java.time.LocalDate

data class CreateStockMarketScheduleRequest(
    val title: String,
    val content: String? = null,
    val scheduleDate: LocalDate
)
