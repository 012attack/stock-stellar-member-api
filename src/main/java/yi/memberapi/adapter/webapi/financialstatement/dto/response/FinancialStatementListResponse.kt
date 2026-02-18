package yi.memberapi.adapter.webapi.financialstatement.dto.response

data class FinancialStatementListResponse(
    val financialStatements: List<FinancialStatementResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
