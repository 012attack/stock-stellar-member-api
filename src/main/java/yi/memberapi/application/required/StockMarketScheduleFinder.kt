package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.schedule.dto.response.StockMarketScheduleDetailResponse
import yi.memberapi.domain.schedule.StockMarketSchedule

interface StockMarketScheduleFinder {
    fun find(id: Int): StockMarketScheduleDetailResponse
    fun findEntityByIdWithMember(id: Int): StockMarketSchedule
}
