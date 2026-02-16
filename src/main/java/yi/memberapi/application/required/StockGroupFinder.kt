package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.response.StockGroupDetailResponse

interface StockGroupFinder {
    fun find(id: Int, memberId: Long): StockGroupDetailResponse
}
