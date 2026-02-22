package yi.memberapi.adapter.webapi.schedule

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.webapi.schedule.dto.response.StockMarketScheduleListResponse
import yi.memberapi.application.required.StockMarketScheduleLister
import java.time.LocalDate

@RestController
@RequestMapping("/api/stock-market-schedules")
class GetStockMarketScheduleListApi(
    private val stockMarketScheduleLister: StockMarketScheduleLister
) {

    @GetMapping
    fun list(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) startDate: LocalDate?,
        @RequestParam(required = false) endDate: LocalDate?
    ): ResponseEntity<StockMarketScheduleListResponse> {
        val response = stockMarketScheduleLister.list(page, size, startDate, endDate)
        return ResponseEntity.ok(response)
    }
}
