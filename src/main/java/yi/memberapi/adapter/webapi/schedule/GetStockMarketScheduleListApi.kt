package yi.memberapi.adapter.webapi.schedule

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.schedule.dto.response.StockMarketScheduleListResponse
import yi.memberapi.application.required.schedule.StockMarketScheduleLister
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
        @RequestParam(required = false) endDate: LocalDate?,
        @RequestParam(required = false) favoriteOnly: Boolean = false
    ): ResponseEntity<StockMarketScheduleListResponse> {
        val memberId = if (favoriteOnly) {
            val memberUserDetails = SecurityContextHolder.getContext().authentication?.principal as? MemberUserDetails
            memberUserDetails?.getMember()?.id
        } else null

        val response = stockMarketScheduleLister.list(page, size, startDate, endDate, favoriteOnly, memberId)
        return ResponseEntity.ok(response)
    }
}
