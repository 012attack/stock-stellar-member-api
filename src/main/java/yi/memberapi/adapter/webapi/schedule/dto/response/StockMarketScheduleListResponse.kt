package yi.memberapi.adapter.webapi.schedule.dto.response

data class StockMarketScheduleListResponse(
    val schedules: List<StockMarketScheduleResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
