package yi.memberapi.adapter.webapi.stocknews

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import yi.memberapi.adapter.webapi.stocknews.dto.request.AddStockNewsRequest
import yi.memberapi.application.required.StockNewsAdder

@RestController
@RequestMapping("/api/stocks")
class AddStockNewsApi(
    private val stockNewsAdder: StockNewsAdder
) {

    @PostMapping("/{stockId}/news")
    fun addNews(
        @PathVariable stockId: Int,
        @RequestBody request: AddStockNewsRequest
    ): ResponseEntity<Void> {
        stockNewsAdder.add(stockId, request.newsIds)
        return ResponseEntity.ok().build()
    }
}
