package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.stock.dto.response.StockListResponse

interface StockLister {
    fun list(page: Int, size: Int, stockName: String?, stockCode: String?, themeName: String?, favoriteOnly: Boolean = false, memberId: Long? = null): StockListResponse
}
