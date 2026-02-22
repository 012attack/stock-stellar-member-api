package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.schedule.StockMarketSchedule
import java.time.LocalDate

interface StockMarketScheduleRepository : JpaRepository<StockMarketSchedule, Int> {

    @Query("""
        SELECT s FROM StockMarketSchedule s
        ORDER BY s.scheduleDate DESC
    """,
        countQuery = """
        SELECT COUNT(s) FROM StockMarketSchedule s
    """)
    fun findAllOrderByScheduleDateDesc(pageable: Pageable): Page<StockMarketSchedule>

    @Query("""
        SELECT s FROM StockMarketSchedule s
        WHERE s.scheduleDate BETWEEN :startDate AND :endDate
        ORDER BY s.scheduleDate DESC
    """,
        countQuery = """
        SELECT COUNT(s) FROM StockMarketSchedule s
        WHERE s.scheduleDate BETWEEN :startDate AND :endDate
    """)
    fun findByScheduleDateBetween(startDate: LocalDate, endDate: LocalDate, pageable: Pageable): Page<StockMarketSchedule>

    @Query("SELECT s FROM StockMarketSchedule s LEFT JOIN FETCH s.member WHERE s.id = :id")
    fun findByIdWithMember(id: Int): StockMarketSchedule?
}
