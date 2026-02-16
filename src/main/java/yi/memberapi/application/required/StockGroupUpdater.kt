package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.request.UpdateStockGroupRequest
import yi.memberapi.adapter.webapi.dto.response.StockGroupResponse

interface StockGroupUpdater {
    fun update(id: Int, request: UpdateStockGroupRequest, memberId: Long): StockGroupResponse
}
