package yi.memberapi.adapter.webapi.investmentcase

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.webapi.investmentcase.dto.response.InvestmentCaseDetailResponse
import yi.memberapi.application.required.InvestmentCaseFinder

@RestController
@RequestMapping("/api/investment-cases")
class GetInvestmentCaseDetailApi(
    private val investmentCaseFinder: InvestmentCaseFinder
) {

    @GetMapping("/{id}")
    fun detail(@PathVariable id: Int): ResponseEntity<InvestmentCaseDetailResponse> {
        val response = investmentCaseFinder.find(id)
        return ResponseEntity.ok(response)
    }
}
