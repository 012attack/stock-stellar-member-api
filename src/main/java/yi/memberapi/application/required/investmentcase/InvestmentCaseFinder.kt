package yi.memberapi.application.required.investmentcase

import yi.memberapi.adapter.webapi.investmentcase.dto.response.InvestmentCaseDetailResponse
import yi.memberapi.domain.investmentcase.InvestmentCase

interface InvestmentCaseFinder {
    fun find(id: Int): InvestmentCaseDetailResponse
    fun findEntityByIdWithMember(id: Int): InvestmentCase
}
