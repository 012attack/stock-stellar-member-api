package yi.memberapi.adapter.webapi

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.application.required.StockGroupStockRemover

@RestController
@RequestMapping("/api/stock-groups")
class RemoveStockGroupStockApi(
    private val stockGroupStockRemover: StockGroupStockRemover
) {

    @DeleteMapping("/{groupId}/stocks/{stockId}")
    fun removeStock(
        @PathVariable groupId: Int,
        @PathVariable stockId: Int
    ): ResponseEntity<Void> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        stockGroupStockRemover.remove(groupId, stockId, memberId)
        return ResponseEntity.noContent().build()
    }
}
