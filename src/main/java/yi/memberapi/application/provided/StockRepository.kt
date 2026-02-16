package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.stock.Stock

interface StockRepository : JpaRepository<Stock, Int> {

    @Query("""
        SELECT DISTINCT s FROM Stock s
        LEFT JOIN s.themes t
        WHERE (:stockName IS NULL OR s.stockName LIKE %:stockName%)
        AND (:stockCode IS NULL OR s.stockCode LIKE %:stockCode%)
        AND (:themeName IS NULL OR t.themeName LIKE %:themeName%)
        ORDER BY s.stockName ASC
    """,
        countQuery = """
        SELECT COUNT(DISTINCT s) FROM Stock s
        LEFT JOIN s.themes t
        WHERE (:stockName IS NULL OR s.stockName LIKE %:stockName%)
        AND (:stockCode IS NULL OR s.stockCode LIKE %:stockCode%)
        AND (:themeName IS NULL OR t.themeName LIKE %:themeName%)
    """)
    fun findWithFilters(
        stockName: String?,
        stockCode: String?,
        themeName: String?,
        pageable: Pageable
    ): Page<Stock>

    fun findByStockCode(stockCode: String): Stock?

    @Query("SELECT s FROM Stock s LEFT JOIN FETCH s.themes WHERE s.id = :id")
    fun findByIdWithThemes(id: Int): Stock?

    @Query("SELECT s FROM Stock s LEFT JOIN FETCH s.themes WHERE s.stockCode = :stockCode")
    fun findByStockCodeWithThemes(stockCode: String): Stock?

    @Query("""
        SELECT DISTINCT s FROM Stock s
        LEFT JOIN FETCH s.themes
        JOIN s.themes t
        WHERE t.id = :themeId
    """)
    fun findByThemeId(themeId: Int): List<Stock>
}
