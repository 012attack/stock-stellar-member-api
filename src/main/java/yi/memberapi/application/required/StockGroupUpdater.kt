package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.stockgroup.dto.request.UpdateStockGroupRequest
import yi.memberapi.adapter.webapi.stockgroup.dto.response.StockGroupResponse

interface StockGroupUpdater {
    fun update(id: Int, request: UpdateStockGroupRequest, memberId: Long): StockGroupResponse
}
