package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.response.StockGroupListResponse

interface StockGroupLister {
    fun list(page: Int, size: Int, memberId: Long): StockGroupListResponse
}
