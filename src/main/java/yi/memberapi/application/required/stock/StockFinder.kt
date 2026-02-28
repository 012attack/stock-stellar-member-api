package yi.memberapi.application.required.stock

import yi.memberapi.adapter.webapi.stock.dto.response.StockResponse
import yi.memberapi.domain.stock.Stock

interface StockFinder {
    fun findById(id: Int): StockResponse?
    fun findByCode(stockCode: String): StockResponse?
    fun findEntityByIdWithThemes(id: Int): Stock
    fun findEntityByIdWithNews(id: Int): Stock
    fun findAllEntitiesByIds(ids: List<Int>): List<Stock>
}
