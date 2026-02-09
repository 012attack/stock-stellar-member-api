package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.response.StockResponse

interface StockFinder {
    fun findById(id: Int): StockResponse?
    fun findByCode(stockCode: String): StockResponse?
}
