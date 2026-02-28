package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.companyannouncement.CompanyAnnouncement

interface CompanyAnnouncementRepository : JpaRepository<CompanyAnnouncement, Int> {

    @Query("""
        SELECT ca FROM CompanyAnnouncement ca
        LEFT JOIN FETCH ca.stock
        WHERE (:stockId IS NULL OR ca.stock.id = :stockId)
        AND (:reportNm IS NULL OR ca.reportNm LIKE %:reportNm%)
        AND (:pblntfTy IS NULL OR CAST(ca.pblntfTy AS string) = :pblntfTy)
        ORDER BY ca.rceptDt DESC
    """,
        countQuery = """
        SELECT COUNT(ca) FROM CompanyAnnouncement ca
        WHERE (:stockId IS NULL OR ca.stock.id = :stockId)
        AND (:reportNm IS NULL OR ca.reportNm LIKE %:reportNm%)
        AND (:pblntfTy IS NULL OR CAST(ca.pblntfTy AS string) = :pblntfTy)
    """)
    fun findWithFilters(
        stockId: Int?,
        reportNm: String?,
        pblntfTy: String?,
        pageable: Pageable
    ): Page<CompanyAnnouncement>

    @Query("SELECT ca FROM CompanyAnnouncement ca LEFT JOIN FETCH ca.stock WHERE ca.id = :id")
    fun findByIdWithStock(id: Int): CompanyAnnouncement?
}
