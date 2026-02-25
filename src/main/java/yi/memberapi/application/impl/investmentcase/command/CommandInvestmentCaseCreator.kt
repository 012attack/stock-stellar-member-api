package yi.memberapi.application.impl.investmentcase.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.investmentcase.dto.request.CreateInvestmentCaseRequest
import yi.memberapi.adapter.webapi.investmentcase.dto.response.InvestmentCaseResponse
import yi.memberapi.application.provided.InvestmentCaseRepository
import yi.memberapi.application.required.InvestmentCaseCreator
import yi.memberapi.application.required.MemberFinder
import yi.memberapi.domain.investmentcase.InvestmentCase

@Service
@Transactional
class CommandInvestmentCaseCreator(
    private val investmentCaseRepository: InvestmentCaseRepository,
    private val memberFinder: MemberFinder
) : InvestmentCaseCreator {

    override fun create(request: CreateInvestmentCaseRequest, memberId: Long): InvestmentCaseResponse {
        val member = memberFinder.findById(memberId)

        val investmentCase = InvestmentCase(
            title = request.title,
            content = request.content,
            resultType = request.resultType,
            member = member
        )

        val saved = investmentCaseRepository.save(investmentCase)

        return InvestmentCaseResponse(
            id = saved.id!!,
            title = saved.title,
            content = saved.content,
            resultType = saved.resultType,
            createdAt = saved.createdAt,
            updatedAt = saved.updatedAt
        )
    }
}
