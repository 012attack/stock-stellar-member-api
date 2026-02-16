package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.financialstatement.FinancialStatement

interface FinancialStatementRepository : JpaRepository<FinancialStatement, Int> {

    @Query("""
        SELECT fs FROM FinancialStatement fs
        LEFT JOIN FETCH fs.stock s
        LEFT JOIN FETCH fs.accountType at
        WHERE (:stockName IS NULL OR s.stockName LIKE %:stockName%)
        AND (:bsnsYear IS NULL OR fs.bsnsYear = :bsnsYear)
        AND (:reprtCode IS NULL OR fs.reprtCode = :reprtCode)
        AND (:fsDiv IS NULL OR fs.fsDiv = :fsDiv)
        ORDER BY fs.bsnsYear DESC, fs.ord ASC
    """,
        countQuery = """
        SELECT COUNT(fs) FROM FinancialStatement fs
        LEFT JOIN fs.stock s
        WHERE (:stockName IS NULL OR s.stockName LIKE %:stockName%)
        AND (:bsnsYear IS NULL OR fs.bsnsYear = :bsnsYear)
        AND (:reprtCode IS NULL OR fs.reprtCode = :reprtCode)
        AND (:fsDiv IS NULL OR fs.fsDiv = :fsDiv)
    """)
    fun findWithFilters(
        stockName: String?,
        bsnsYear: String?,
        reprtCode: String?,
        fsDiv: String?,
        pageable: Pageable
    ): Page<FinancialStatement>
}
