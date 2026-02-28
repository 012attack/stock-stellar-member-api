package yi.memberapi.application.required.investmentcase

import yi.memberapi.adapter.webapi.investmentcase.dto.response.InvestmentCaseListResponse
import yi.memberapi.domain.investmentcase.InvestmentResultType

interface InvestmentCaseLister {
    fun list(page: Int, size: Int, resultType: InvestmentResultType?): InvestmentCaseListResponse
}
