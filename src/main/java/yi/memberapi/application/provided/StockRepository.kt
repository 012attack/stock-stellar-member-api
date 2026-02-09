package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.stock.Stock

interface StockRepository : JpaRepository<Stock, Int> {

    @Query("""
        SELECT s FROM Stock s
        WHERE (:stockName IS NULL OR s.stockName LIKE %:stockName%)
        AND (:stockCode IS NULL OR s.stockCode LIKE %:stockCode%)
        ORDER BY s.stockName ASC
    """,
        countQuery = """
        SELECT COUNT(s) FROM Stock s
        WHERE (:stockName IS NULL OR s.stockName LIKE %:stockName%)
        AND (:stockCode IS NULL OR s.stockCode LIKE %:stockCode%)
    """)
    fun findWithFilters(
        stockName: String?,
        stockCode: String?,
        pageable: Pageable
    ): Page<Stock>

    fun findByStockCode(stockCode: String): Stock?
}
