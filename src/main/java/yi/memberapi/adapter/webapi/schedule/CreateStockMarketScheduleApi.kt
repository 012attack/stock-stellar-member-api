package yi.memberapi.adapter.webapi.schedule

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.schedule.dto.request.CreateStockMarketScheduleRequest
import yi.memberapi.adapter.webapi.schedule.dto.response.StockMarketScheduleResponse
import yi.memberapi.application.required.StockMarketScheduleCreator
import java.net.URI

@RestController
@RequestMapping("/api/stock-market-schedules")
class CreateStockMarketScheduleApi(
    private val stockMarketScheduleCreator: StockMarketScheduleCreator
) {

    @PostMapping
    fun create(@RequestBody request: CreateStockMarketScheduleRequest): ResponseEntity<StockMarketScheduleResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!
        val response = stockMarketScheduleCreator.create(request, memberId)
        return ResponseEntity.created(URI.create("/api/stock-market-schedules/${response.id}")).body(response)
    }
}
