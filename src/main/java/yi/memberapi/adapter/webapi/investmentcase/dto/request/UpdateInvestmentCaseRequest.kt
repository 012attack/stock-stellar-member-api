package yi.memberapi.adapter.webapi.investmentcase.dto.request

import yi.memberapi.domain.investmentcase.InvestmentResultType

data class UpdateInvestmentCaseRequest(
    val title: String,
    val content: String,
    val resultType: InvestmentResultType
)
