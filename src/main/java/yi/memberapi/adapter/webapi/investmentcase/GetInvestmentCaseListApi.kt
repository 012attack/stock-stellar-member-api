package yi.memberapi.adapter.webapi.investmentcase

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.webapi.investmentcase.dto.response.InvestmentCaseListResponse
import yi.memberapi.application.required.investmentcase.InvestmentCaseLister
import yi.memberapi.domain.investmentcase.InvestmentResultType

@RestController
@RequestMapping("/api/investment-cases")
class GetInvestmentCaseListApi(
    private val investmentCaseLister: InvestmentCaseLister
) {

    @GetMapping
    fun list(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) resultType: InvestmentResultType?
    ): ResponseEntity<InvestmentCaseListResponse> {
        val response = investmentCaseLister.list(page, size, resultType)
        return ResponseEntity.ok(response)
    }
}
