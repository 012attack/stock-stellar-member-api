package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.response.StockResponse
import yi.memberapi.domain.stock.Stock

interface StockFinder {
    fun findById(id: Int): StockResponse?
    fun findByCode(stockCode: String): StockResponse?
    fun findEntityByIdWithThemes(id: Int): Stock
    fun findAllEntitiesByIds(ids: List<Int>): List<Stock>
}
