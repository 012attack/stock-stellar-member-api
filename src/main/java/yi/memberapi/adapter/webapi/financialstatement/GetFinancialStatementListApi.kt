package yi.memberapi.adapter.webapi.financialstatement

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.webapi.financialstatement.dto.response.FinancialStatementListResponse
import yi.memberapi.application.required.FinancialStatementLister

@RestController
@RequestMapping("/api/financial-statements")
class GetFinancialStatementListApi(
    private val financialStatementLister: FinancialStatementLister
) {

    @GetMapping
    fun getFinancialStatements(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) stockName: String?,
        @RequestParam(required = false) bsnsYear: String?,
        @RequestParam(required = false) reprtCode: String?,
        @RequestParam(required = false) fsDiv: String?
    ): ResponseEntity<FinancialStatementListResponse> {
        val response = financialStatementLister.list(page, size, stockName, bsnsYear, reprtCode, fsDiv)
        return ResponseEntity.ok(response)
    }
}
