package yi.memberapi.adapter.webapi.investmentcase.dto.response

import yi.memberapi.domain.investmentcase.InvestmentResultType
import java.time.LocalDateTime

data class InvestmentCaseDetailResponse(
    val id: Int,
    val title: String,
    val content: String,
    val resultType: InvestmentResultType,
    val memberName: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
