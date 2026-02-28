package yi.memberapi.application.required.investmentcase

import yi.memberapi.adapter.webapi.investmentcase.dto.request.CreateInvestmentCaseRequest
import yi.memberapi.adapter.webapi.investmentcase.dto.response.InvestmentCaseResponse

interface InvestmentCaseCreator {
    fun create(request: CreateInvestmentCaseRequest, memberId: Long): InvestmentCaseResponse
}
