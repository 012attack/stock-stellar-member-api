package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.schedule.dto.response.StockMarketScheduleListResponse
import java.time.LocalDate

interface StockMarketScheduleLister {
    fun list(page: Int, size: Int, startDate: LocalDate?, endDate: LocalDate?): StockMarketScheduleListResponse
}
