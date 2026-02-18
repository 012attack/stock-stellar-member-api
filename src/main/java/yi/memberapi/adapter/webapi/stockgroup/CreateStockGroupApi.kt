package yi.memberapi.adapter.webapi.stockgroup

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.stockgroup.dto.request.CreateStockGroupRequest
import yi.memberapi.adapter.webapi.stockgroup.dto.response.StockGroupResponse
import yi.memberapi.application.required.StockGroupCreator
import java.net.URI

@RestController
@RequestMapping("/api/stock-groups")
class CreateStockGroupApi(
    private val stockGroupCreator: StockGroupCreator
) {

    @PostMapping
    fun create(
        @RequestBody request: CreateStockGroupRequest
    ): ResponseEntity<StockGroupResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        val response = stockGroupCreator.create(request, memberId)
        return ResponseEntity.created(URI.create("/api/stock-groups/${response.id}")).body(response)
    }
}
