package yi.memberapi.adapter.webapi.financialstatement.dto.response

import yi.memberapi.adapter.webapi.stock.dto.response.StockResponse
import java.time.LocalDateTime

data class FinancialStatementResponse(
    val id: Int,
    val stock: StockResponse?,
    val rceptNo: String,
    val reprtCode: String,
    val bsnsYear: String,
    val corpCode: String?,
    val fsDiv: String,
    val sjDiv: String,
    val sjNm: String?,
    val accountId: String?,
    val accountNm: String,
    val accountDetail: String?,
    val thstrmNm: String?,
    val thstrmAmount: Long?,
    val thstrmAddAmount: Long?,
    val frmtrmNm: String?,
    val frmtrmAmount: Long?,
    val frmtrmQNm: String?,
    val frmtrmQAmount: Long?,
    val frmtrmAddAmount: Long?,
    val bfefrmtrmNm: String?,
    val bfefrmtrmAmount: Long?,
    val ord: Int?,
    val currency: String?,
    val createdAt: LocalDateTime?
)
