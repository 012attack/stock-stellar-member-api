package yi.memberapi.application.impl.investmentcase.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.investmentcase.dto.response.InvestmentCaseListResponse
import yi.memberapi.adapter.webapi.investmentcase.dto.response.InvestmentCaseResponse
import yi.memberapi.application.provided.InvestmentCaseRepository
import yi.memberapi.application.required.InvestmentCaseLister
import yi.memberapi.domain.investmentcase.InvestmentResultType

@Service
@Transactional(readOnly = true)
class QueryInvestmentCaseLister(
    private val investmentCaseRepository: InvestmentCaseRepository
) : InvestmentCaseLister {

    override fun list(page: Int, size: Int, resultType: InvestmentResultType?): InvestmentCaseListResponse {
        val pageable = PageRequest.of(page, size)

        val investmentCasePage = if (resultType != null) {
            investmentCaseRepository.findByResultTypeOrderByCreatedAtDesc(resultType, pageable)
        } else {
            investmentCaseRepository.findAllOrderByCreatedAtDesc(pageable)
        }

        val investmentCaseList = investmentCasePage.content.map { investmentCase ->
            InvestmentCaseResponse(
                id = investmentCase.id!!,
                title = investmentCase.title,
                content = investmentCase.content,
                resultType = investmentCase.resultType,
                createdAt = investmentCase.createdAt,
                updatedAt = investmentCase.updatedAt
            )
        }

        return InvestmentCaseListResponse(
            investmentCases = investmentCaseList,
            page = investmentCasePage.number,
            size = investmentCasePage.size,
            totalElements = investmentCasePage.totalElements,
            totalPages = investmentCasePage.totalPages
        )
    }
}
