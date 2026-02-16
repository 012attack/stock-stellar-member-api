package yi.memberapi.application.financialstatement.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.response.FinancialStatementListResponse
import yi.memberapi.adapter.webapi.dto.response.FinancialStatementResponse
import yi.memberapi.adapter.webapi.dto.response.StockResponse
import yi.memberapi.application.provided.FinancialStatementRepository
import yi.memberapi.application.required.FinancialStatementLister

@Service
@Transactional(readOnly = true)
class QueryFinancialStatementLister(
    private val financialStatementRepository: FinancialStatementRepository
) : FinancialStatementLister {

    override fun list(
        page: Int,
        size: Int,
        stockName: String?,
        bsnsYear: String?,
        reprtCode: String?,
        fsDiv: String?
    ): FinancialStatementListResponse {
        val pageable = PageRequest.of(page, size)
        val resultPage = financialStatementRepository.findWithFilters(
            stockName, bsnsYear, reprtCode, fsDiv, pageable
        )

        val items = resultPage.content.map { fs ->
            FinancialStatementResponse(
                id = fs.id!!,
                stock = fs.stock?.let {
                    StockResponse(
                        id = it.id!!,
                        stockCode = it.stockCode,
                        stockName = it.stockName,
                        companySummary = it.companySummary
                    )
                },
                rceptNo = fs.rceptNo,
                reprtCode = fs.reprtCode,
                bsnsYear = fs.bsnsYear,
                corpCode = fs.corpCode,
                fsDiv = fs.fsDiv,
                sjDiv = fs.sjDiv,
                sjNm = fs.sjNm,
                accountId = fs.accountId,
                accountNm = fs.accountType?.accountNm ?: "",
                accountDetail = fs.accountDetail,
                thstrmNm = fs.thstrmNm,
                thstrmAmount = fs.thstrmAmount,
                thstrmAddAmount = fs.thstrmAddAmount,
                frmtrmNm = fs.frmtrmNm,
                frmtrmAmount = fs.frmtrmAmount,
                frmtrmQNm = fs.frmtrmQNm,
                frmtrmQAmount = fs.frmtrmQAmount,
                frmtrmAddAmount = fs.frmtrmAddAmount,
                bfefrmtrmNm = fs.bfefrmtrmNm,
                bfefrmtrmAmount = fs.bfefrmtrmAmount,
                ord = fs.ord,
                currency = fs.currency,
                createdAt = fs.createdAt
            )
        }

        return FinancialStatementListResponse(
            financialStatements = items,
            page = resultPage.number,
            size = resultPage.size,
            totalElements = resultPage.totalElements,
            totalPages = resultPage.totalPages
        )
    }
}
