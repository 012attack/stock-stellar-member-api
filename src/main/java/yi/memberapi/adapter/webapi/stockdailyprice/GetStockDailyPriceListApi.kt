package yi.memberapi.adapter.webapi.stockdailyprice

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import yi.memberapi.adapter.webapi.stockdailyprice.dto.response.StockDailyPriceListResponse
import yi.memberapi.application.required.stock.StockFinder
import yi.memberapi.application.required.stockdailyprice.StockDailyPriceLister
import java.time.LocalDate

@RestController
@RequestMapping("/api/stocks")
class GetStockDailyPriceListApi(
    private val stockDailyPriceLister: StockDailyPriceLister,
    private val stockFinder: StockFinder
) {

    @GetMapping("/{stockId}/daily-prices")
    fun getDailyPricesByStockId(
        @PathVariable stockId: Int,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "30") size: Int
    ): ResponseEntity<StockDailyPriceListResponse> {
        val response = stockDailyPriceLister.list(stockId, startDate, endDate, page, size)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/code/{stockCode}/daily-prices")
    fun getDailyPricesByStockCode(
        @PathVariable stockCode: String,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "30") size: Int
    ): ResponseEntity<StockDailyPriceListResponse> {
        val stock = stockFinder.findByCode(stockCode)
            ?: return ResponseEntity.notFound().build()

        val response = stockDailyPriceLister.list(stock.id, startDate, endDate, page, size)
        return ResponseEntity.ok(response)
    }
}
