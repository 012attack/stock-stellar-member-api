package yi.memberapi.adapter.webapi.stocknews

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import yi.memberapi.application.required.stock.StockNewsRemover

@RestController
@RequestMapping("/api/stocks")
class RemoveStockNewsApi(
    private val stockNewsRemover: StockNewsRemover
) {

    @DeleteMapping("/{stockId}/news/{newsId}")
    fun removeNews(
        @PathVariable stockId: Int,
        @PathVariable newsId: Int
    ): ResponseEntity<Void> {
        stockNewsRemover.remove(stockId, newsId)
        return ResponseEntity.noContent().build()
    }
}
