package yi.memberapi.adapter.webapi.stockgroup

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.application.required.StockGroupDeleter

@RestController
@RequestMapping("/api/stock-groups")
class DeleteStockGroupApi(
    private val stockGroupDeleter: StockGroupDeleter
) {

    @DeleteMapping("/{id}")
    fun delete(
        @PathVariable id: Int
    ): ResponseEntity<Void> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        stockGroupDeleter.delete(id, memberId)
        return ResponseEntity.noContent().build()
    }
}
