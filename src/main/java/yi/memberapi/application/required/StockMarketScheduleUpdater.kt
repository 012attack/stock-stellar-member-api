package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.schedule.dto.request.UpdateStockMarketScheduleRequest
import yi.memberapi.adapter.webapi.schedule.dto.response.StockMarketScheduleResponse

interface StockMarketScheduleUpdater {
    fun update(id: Int, request: UpdateStockMarketScheduleRequest, memberId: Long): StockMarketScheduleResponse
}
