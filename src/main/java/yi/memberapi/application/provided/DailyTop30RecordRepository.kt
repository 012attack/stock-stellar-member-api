package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.record.DailyTop30Record
import java.time.LocalDate

interface DailyTop30RecordRepository : JpaRepository<DailyTop30Record, Int> {

    @Query("SELECT r FROM DailyTop30Record r LEFT JOIN FETCH r.stock LEFT JOIN FETCH r.themes WHERE r.recordDate BETWEEN :startDate AND :endDate ORDER BY r.recordDate DESC, r.rank ASC")
    fun findByRecordDateBetweenWithStockAndThemes(startDate: LocalDate, endDate: LocalDate): List<DailyTop30Record>

    @Query("""
        SELECT DISTINCT r FROM DailyTop30Record r
        LEFT JOIN FETCH r.stock s
        LEFT JOIN FETCH r.themes t
        WHERE r.recordDate BETWEEN :startDate AND :endDate
        AND (:stockName IS NULL OR s.stockName LIKE %:stockName%)
        AND (:stockCode IS NULL OR s.stockCode LIKE %:stockCode%)
        AND (:themeName IS NULL OR t.themeName LIKE %:themeName%)
        ORDER BY r.recordDate DESC, r.rank ASC
    """)
    fun findByDateRangeWithFilters(
        startDate: LocalDate,
        endDate: LocalDate,
        stockName: String?,
        stockCode: String?,
        themeName: String?
    ): List<DailyTop30Record>

    fun findByRecordDate(recordDate: LocalDate, pageable: Pageable): Page<DailyTop30Record>

    @Query("SELECT DISTINCT r.recordDate FROM DailyTop30Record r ORDER BY r.recordDate DESC")
    fun findDistinctRecordDates(pageable: Pageable): List<LocalDate>
}
