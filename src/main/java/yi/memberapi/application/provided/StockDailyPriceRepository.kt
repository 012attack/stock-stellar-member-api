package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import yi.memberapi.domain.stockdailyprice.StockDailyPrice
import java.time.LocalDate

interface StockDailyPriceRepository : JpaRepository<StockDailyPrice, Int> {

    fun findByStockIdOrderByTradeDateDesc(stockId: Int, pageable: Pageable): Page<StockDailyPrice>

    fun findByStockIdAndTradeDateBetweenOrderByTradeDateDesc(
        stockId: Int,
        startDate: LocalDate,
        endDate: LocalDate,
        pageable: Pageable
    ): Page<StockDailyPrice>
}
