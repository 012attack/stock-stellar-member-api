package yi.memberapi.adapter.webapi

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.webapi.dto.response.StockResponse
import yi.memberapi.application.required.StockFinder

@RestController
@RequestMapping("/api/stocks")
class GetStockDetailApi(
    private val stockFinder: StockFinder
) {

    @GetMapping("/{id}")
    fun getStockById(@PathVariable id: Int): ResponseEntity<StockResponse> {
        val stock = stockFinder.findById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(stock)
    }

    @GetMapping("/code/{stockCode}")
    fun getStockByCode(@PathVariable stockCode: String): ResponseEntity<StockResponse> {
        val stock = stockFinder.findByCode(stockCode)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(stock)
    }
}
