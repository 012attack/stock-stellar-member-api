package yi.memberapi.adapter.webapi

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.dto.request.UpdateStockGroupRequest
import yi.memberapi.adapter.webapi.dto.response.StockGroupResponse
import yi.memberapi.application.required.StockGroupUpdater

@RestController
@RequestMapping("/api/stock-groups")
class UpdateStockGroupApi(
    private val stockGroupUpdater: StockGroupUpdater
) {

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Int,
        @RequestBody request: UpdateStockGroupRequest
    ): ResponseEntity<StockGroupResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        val response = stockGroupUpdater.update(id, request, memberId)
        return ResponseEntity.ok(response)
    }
}
