package yi.memberapi.application.impl.investmentcase.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.provided.InvestmentCaseRepository
import yi.memberapi.application.required.investmentcase.InvestmentCaseDeleter
import yi.memberapi.application.required.investmentcase.InvestmentCaseFinder

@Service
@Transactional
class CommandInvestmentCaseDeleter(
    private val investmentCaseRepository: InvestmentCaseRepository,
    private val investmentCaseFinder: InvestmentCaseFinder
) : InvestmentCaseDeleter {

    override fun delete(id: Int, memberId: Long) {
        val investmentCase = investmentCaseFinder.findEntityByIdWithMember(id)

        if (investmentCase.member?.id != memberId) {
            throw IllegalArgumentException("Not authorized to delete this investment case")
        }

        investmentCaseRepository.delete(investmentCase)
    }
}
