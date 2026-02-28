package yi.memberapi.domain.companyannouncement

import jakarta.persistence.*
import yi.memberapi.domain.stock.Stock
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "company_announcements")
class CompanyAnnouncement(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    val stock: Stock,

    @Column(name = "rcept_no", nullable = false, length = 20, unique = true)
    val rceptNo: String,

    @Column(name = "corp_code", length = 10)
    val corpCode: String? = null,

    @Column(name = "report_nm", nullable = false, columnDefinition = "TEXT")
    val reportNm: String,

    @Column(name = "flr_nm", length = 100)
    val flrNm: String? = null,

    @Column(name = "rcept_dt", nullable = false)
    val rceptDt: LocalDate,

    @Column(name = "pblntf_ty")
    @Enumerated(EnumType.STRING)
    val pblntfTy: AnnouncementType? = null,

    @Column(name = "pblntf_detail_ty", length = 10)
    val pblntfDetailTy: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime? = null
) {
    protected constructor() : this(
        null, Stock(null, "", ""), "", null, "", null,
        LocalDate.now(), null, null, null
    )
}
