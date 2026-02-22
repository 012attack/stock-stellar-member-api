package yi.memberapi.adapter.webapi.schedule

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.schedule.dto.request.UpdateStockMarketScheduleRequest
import yi.memberapi.adapter.webapi.schedule.dto.response.StockMarketScheduleResponse
import yi.memberapi.application.required.StockMarketScheduleUpdater

@RestController
@RequestMapping("/api/stock-market-schedules")
class UpdateStockMarketScheduleApi(
    private val stockMarketScheduleUpdater: StockMarketScheduleUpdater
) {

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Int,
        @RequestBody request: UpdateStockMarketScheduleRequest
    ): ResponseEntity<StockMarketScheduleResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!
        val response = stockMarketScheduleUpdater.update(id, request, memberId)
        return ResponseEntity.ok(response)
    }
}
