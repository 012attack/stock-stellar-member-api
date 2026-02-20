package yi.memberapi.adapter.webapi.stock

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.stock.dto.response.StockListResponse
import yi.memberapi.application.required.StockLister

@RestController
@RequestMapping("/api/stocks")
class GetStockListApi(
    private val stockLister: StockLister
) {

    @GetMapping
    fun getStocks(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) stockName: String?,
        @RequestParam(required = false) stockCode: String?,
        @RequestParam(required = false) themeName: String?,
        @RequestParam(required = false) favoriteOnly: Boolean = false
    ): ResponseEntity<StockListResponse> {
        val memberId = if (favoriteOnly) {
            val memberUserDetails = SecurityContextHolder.getContext().authentication?.principal as? MemberUserDetails
            memberUserDetails?.getMember()?.id
        } else null

        val response = stockLister.list(page, size, stockName, stockCode, themeName, favoriteOnly, memberId)
        return ResponseEntity.ok(response)
    }
}
