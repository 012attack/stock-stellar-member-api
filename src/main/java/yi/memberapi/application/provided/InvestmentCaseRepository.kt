package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.investmentcase.InvestmentCase
import yi.memberapi.domain.investmentcase.InvestmentResultType

interface InvestmentCaseRepository : JpaRepository<InvestmentCase, Int> {

    @Query("""
        SELECT ic FROM InvestmentCase ic
        ORDER BY ic.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(ic) FROM InvestmentCase ic
    """)
    fun findAllOrderByCreatedAtDesc(pageable: Pageable): Page<InvestmentCase>

    @Query("""
        SELECT ic FROM InvestmentCase ic
        WHERE ic.resultType = :resultType
        ORDER BY ic.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(ic) FROM InvestmentCase ic
        WHERE ic.resultType = :resultType
    """)
    fun findByResultTypeOrderByCreatedAtDesc(resultType: InvestmentResultType, pageable: Pageable): Page<InvestmentCase>

    @Query("SELECT ic FROM InvestmentCase ic LEFT JOIN FETCH ic.member WHERE ic.id = :id")
    fun findByIdWithMember(id: Int): InvestmentCase?
}
