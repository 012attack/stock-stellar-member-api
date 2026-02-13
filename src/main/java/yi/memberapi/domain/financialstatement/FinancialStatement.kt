package yi.memberapi.domain.financialstatement

import jakarta.persistence.*
import yi.memberapi.domain.stock.Stock
import java.time.LocalDateTime

@Entity
@Table(name = "financial_statements")
class FinancialStatement(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    val stock: Stock? = null,

    @Column(name = "rcept_no", nullable = false, length = 20)
    val rceptNo: String,

    @Column(name = "reprt_code", nullable = false, length = 10)
    val reprtCode: String,

    @Column(name = "bsns_year", nullable = false, length = 4)
    val bsnsYear: String,

    @Column(name = "corp_code", length = 10)
    val corpCode: String? = null,

    @Column(name = "fs_div", nullable = false, length = 5)
    val fsDiv: String,

    @Column(name = "sj_div", nullable = false, length = 5)
    val sjDiv: String,

    @Column(name = "sj_nm", length = 50)
    val sjNm: String? = null,

    @Column(name = "account_id", length = 100)
    val accountId: String? = null,

    @Column(name = "account_nm", nullable = false, length = 200)
    val accountNm: String,

    @Column(name = "account_detail", length = 500)
    val accountDetail: String? = null,

    @Column(name = "thstrm_nm", length = 50)
    val thstrmNm: String? = null,

    @Column(name = "thstrm_amount")
    val thstrmAmount: Long? = null,

    @Column(name = "thstrm_add_amount")
    val thstrmAddAmount: Long? = null,

    @Column(name = "frmtrm_nm", length = 50)
    val frmtrmNm: String? = null,

    @Column(name = "frmtrm_amount")
    val frmtrmAmount: Long? = null,

    @Column(name = "frmtrm_q_nm", length = 50)
    val frmtrmQNm: String? = null,

    @Column(name = "frmtrm_q_amount")
    val frmtrmQAmount: Long? = null,

    @Column(name = "frmtrm_add_amount")
    val frmtrmAddAmount: Long? = null,

    @Column(name = "bfefrmtrm_nm", length = 50)
    val bfefrmtrmNm: String? = null,

    @Column(name = "bfefrmtrm_amount")
    val bfefrmtrmAmount: Long? = null,

    @Column(name = "ord")
    val ord: Int? = null,

    @Column(name = "currency", length = 10)
    val currency: String? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null
) {
    protected constructor() : this(
        null, null, "", "", "", null, "", "", null, null, "",
        null, null, null, null, null, null, null, null, null, null, null, null, null, null
    )
}
