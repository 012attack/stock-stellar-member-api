package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.financialstatement.dto.response.FinancialStatementListResponse

interface FinancialStatementLister {
    fun list(
        page: Int,
        size: Int,
        stockName: String?,
        bsnsYear: String?,
        reprtCode: String?,
        fsDiv: String?
    ): FinancialStatementListResponse
}
