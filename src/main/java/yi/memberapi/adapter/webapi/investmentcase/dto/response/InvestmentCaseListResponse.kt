package yi.memberapi.adapter.webapi.investmentcase.dto.response

data class InvestmentCaseListResponse(
    val investmentCases: List<InvestmentCaseResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
