package yi.memberapi.application.required.investmentcase

interface InvestmentCaseDeleter {
    fun delete(id: Int, memberId: Long)
}
