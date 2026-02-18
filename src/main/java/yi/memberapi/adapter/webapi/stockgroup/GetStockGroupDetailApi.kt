package yi.memberapi.adapter.webapi.stockgroup

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.stockgroup.dto.response.StockGroupDetailResponse
import yi.memberapi.application.required.StockGroupFinder

@RestController
@RequestMapping("/api/stock-groups")
class GetStockGroupDetailApi(
    private val stockGroupFinder: StockGroupFinder
) {

    @GetMapping("/{id}")
    fun detail(
        @PathVariable id: Int
    ): ResponseEntity<StockGroupDetailResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        val response = stockGroupFinder.find(id, memberId)
        return ResponseEntity.ok(response)
    }
}
