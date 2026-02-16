package yi.memberapi.adapter.webapi

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import yi.memberapi.application.required.StockThemeRemover

@RestController
@RequestMapping("/api/stocks")
class RemoveStockThemeApi(
    private val stockThemeRemover: StockThemeRemover
) {

    @DeleteMapping("/{stockId}/themes/{themeId}")
    fun removeTheme(
        @PathVariable stockId: Int,
        @PathVariable themeId: Int
    ): ResponseEntity<Void> {
        stockThemeRemover.remove(stockId, themeId)
        return ResponseEntity.noContent().build()
    }
}
