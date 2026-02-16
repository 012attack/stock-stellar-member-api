package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.response.StockGroupDetailResponse
import yi.memberapi.domain.stockgroup.StockGroup

interface StockGroupFinder {
    fun find(id: Int, memberId: Long): StockGroupDetailResponse
    fun findEntityByIdWithMember(id: Int): StockGroup
    fun findEntityByIdWithStocks(id: Int): StockGroup
}
