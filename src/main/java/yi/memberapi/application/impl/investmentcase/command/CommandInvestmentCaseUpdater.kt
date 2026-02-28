package yi.memberapi.application.impl.investmentcase.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.investmentcase.dto.request.UpdateInvestmentCaseRequest
import yi.memberapi.adapter.webapi.investmentcase.dto.response.InvestmentCaseResponse
import yi.memberapi.application.required.investmentcase.InvestmentCaseFinder
import yi.memberapi.application.required.investmentcase.InvestmentCaseUpdater

@Service
@Transactional
class CommandInvestmentCaseUpdater(
    private val investmentCaseFinder: InvestmentCaseFinder
) : InvestmentCaseUpdater {

    override fun update(id: Int, request: UpdateInvestmentCaseRequest, memberId: Long): InvestmentCaseResponse {
        val investmentCase = investmentCaseFinder.findEntityByIdWithMember(id)

        if (investmentCase.member?.id != memberId) {
            throw IllegalArgumentException("Not authorized to update this investment case")
        }

        investmentCase.title = request.title
        investmentCase.content = request.content
        investmentCase.resultType = request.resultType

        return InvestmentCaseResponse(
            id = investmentCase.id!!,
            title = investmentCase.title,
            content = investmentCase.content,
            resultType = investmentCase.resultType,
            createdAt = investmentCase.createdAt,
            updatedAt = investmentCase.updatedAt
        )
    }
}
