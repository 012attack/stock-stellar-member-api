package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.request.CreateStockGroupRequest
import yi.memberapi.adapter.webapi.dto.response.StockGroupResponse

interface StockGroupCreator {
    fun create(request: CreateStockGroupRequest, memberId: Long): StockGroupResponse
}
