package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.record.DailyTop30Record
import java.time.LocalDate

interface DailyTop30RecordRepository : JpaRepository<DailyTop30Record, Int> {

    @Query("SELECT r FROM DailyTop30Record r LEFT JOIN FETCH r.stock LEFT JOIN FETCH r.themes WHERE r.recordDate = :recordDate ORDER BY r.rank ASC")
    fun findByRecordDateWithStockAndThemes(recordDate: LocalDate): List<DailyTop30Record>

    fun findByRecordDate(recordDate: LocalDate, pageable: Pageable): Page<DailyTop30Record>

    @Query("SELECT DISTINCT r.recordDate FROM DailyTop30Record r ORDER BY r.recordDate DESC")
    fun findDistinctRecordDates(pageable: Pageable): List<LocalDate>
}
