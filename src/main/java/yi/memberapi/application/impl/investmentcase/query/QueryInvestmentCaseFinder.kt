package yi.memberapi.application.impl.investmentcase.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.investmentcase.dto.response.InvestmentCaseDetailResponse
import yi.memberapi.application.provided.InvestmentCaseRepository
import yi.memberapi.application.required.investmentcase.InvestmentCaseFinder
import yi.memberapi.domain.investmentcase.InvestmentCase

@Service
@Transactional(readOnly = true)
class QueryInvestmentCaseFinder(
    private val investmentCaseRepository: InvestmentCaseRepository
) : InvestmentCaseFinder {

    override fun findEntityByIdWithMember(id: Int): InvestmentCase {
        return investmentCaseRepository.findByIdWithMember(id)
            ?: throw IllegalArgumentException("InvestmentCase not found: $id")
    }

    override fun find(id: Int): InvestmentCaseDetailResponse {
        val investmentCase = investmentCaseRepository.findByIdWithMember(id)
            ?: throw IllegalArgumentException("InvestmentCase not found: $id")

        return InvestmentCaseDetailResponse(
            id = investmentCase.id!!,
            title = investmentCase.title,
            content = investmentCase.content,
            resultType = investmentCase.resultType,
            memberName = investmentCase.member?.name,
            createdAt = investmentCase.createdAt,
            updatedAt = investmentCase.updatedAt
        )
    }
}
