package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.stockgroup.dto.request.CreateStockGroupRequest
import yi.memberapi.adapter.webapi.stockgroup.dto.response.StockGroupResponse

interface StockGroupCreator {
    fun create(request: CreateStockGroupRequest, memberId: Long): StockGroupResponse
}
