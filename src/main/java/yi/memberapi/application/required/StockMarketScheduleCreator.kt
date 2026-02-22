package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.schedule.dto.request.CreateStockMarketScheduleRequest
import yi.memberapi.adapter.webapi.schedule.dto.response.StockMarketScheduleResponse

interface StockMarketScheduleCreator {
    fun create(request: CreateStockMarketScheduleRequest, memberId: Long): StockMarketScheduleResponse
}
