package yi.memberapi.application.required.investmentcase

import yi.memberapi.adapter.webapi.investmentcase.dto.request.UpdateInvestmentCaseRequest
import yi.memberapi.adapter.webapi.investmentcase.dto.response.InvestmentCaseResponse

interface InvestmentCaseUpdater {
    fun update(id: Int, request: UpdateInvestmentCaseRequest, memberId: Long): InvestmentCaseResponse
}
