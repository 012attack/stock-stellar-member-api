package yi.memberapi.adapter.webapi

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.dto.request.AddStockGroupStockRequest
import yi.memberapi.application.required.StockGroupStockAdder

@RestController
@RequestMapping("/api/stock-groups")
class AddStockGroupStockApi(
    private val stockGroupStockAdder: StockGroupStockAdder
) {

    @PostMapping("/{groupId}/stocks")
    fun addStocks(
        @PathVariable groupId: Int,
        @RequestBody request: AddStockGroupStockRequest
    ): ResponseEntity<Void> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        stockGroupStockAdder.add(groupId, request.stockIds, memberId)
        return ResponseEntity.ok().build()
    }
}
