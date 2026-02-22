package yi.memberapi.adapter.webapi.schedule

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.webapi.schedule.dto.response.StockMarketScheduleDetailResponse
import yi.memberapi.application.required.StockMarketScheduleFinder

@RestController
@RequestMapping("/api/stock-market-schedules")
class GetStockMarketScheduleDetailApi(
    private val stockMarketScheduleFinder: StockMarketScheduleFinder
) {

    @GetMapping("/{id}")
    fun detail(@PathVariable id: Int): ResponseEntity<StockMarketScheduleDetailResponse> {
        val response = stockMarketScheduleFinder.find(id)
        return ResponseEntity.ok(response)
    }
}
