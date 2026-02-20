package yi.memberapi.adapter.webapi.stockgroup

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.stockgroup.dto.response.StockGroupListResponse
import yi.memberapi.application.required.StockGroupLister

@RestController
@RequestMapping("/api/stock-groups")
class GetStockGroupListApi(
    private val stockGroupLister: StockGroupLister
) {

    @GetMapping
    fun list(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) favoriteOnly: Boolean = false
    ): ResponseEntity<StockGroupListResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        val response = stockGroupLister.list(page, size, memberId, favoriteOnly)
        return ResponseEntity.ok(response)
    }
}
