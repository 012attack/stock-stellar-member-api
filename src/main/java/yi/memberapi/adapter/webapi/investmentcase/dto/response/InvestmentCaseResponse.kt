package yi.memberapi.adapter.webapi.investmentcase.dto.response

import yi.memberapi.domain.investmentcase.InvestmentResultType
import java.time.LocalDateTime

data class InvestmentCaseResponse(
    val id: Int,
    val title: String,
    val content: String,
    val resultType: InvestmentResultType,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
