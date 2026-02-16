package yi.memberapi.adapter.webapi

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import yi.memberapi.adapter.webapi.dto.request.AddStockThemeRequest
import yi.memberapi.application.required.StockThemeAdder

@RestController
@RequestMapping("/api/stocks")
class AddStockThemeApi(
    private val stockThemeAdder: StockThemeAdder
) {

    @PostMapping("/{stockId}/themes")
    fun addThemes(
        @PathVariable stockId: Int,
        @RequestBody request: AddStockThemeRequest
    ): ResponseEntity<Void> {
        stockThemeAdder.add(stockId, request.themeIds)
        return ResponseEntity.ok().build()
    }
}
