package yi.memberapi.domain.stockdailyprice

import jakarta.persistence.*
import yi.memberapi.domain.stock.Stock
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "stock_daily_prices")
class StockDailyPrice(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id", nullable = false)
    val stock: Stock? = null,

    @Column(name = "trade_date", nullable = false)
    val tradeDate: LocalDate,

    @Column(name = "open_price")
    val openPrice: Long? = null,

    @Column(name = "high_price")
    val highPrice: Long? = null,

    @Column(name = "low_price")
    val lowPrice: Long? = null,

    @Column(name = "close_price")
    val closePrice: Long? = null,

    @Column(name = "volume")
    val volume: Long? = null,

    @Column(name = "trading_value")
    val tradingValue: Long? = null,

    @Column(name = "change_rate", precision = 10, scale = 4)
    val changeRate: BigDecimal? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null
) {
    protected constructor() : this(
        null, null, LocalDate.now(), null, null, null, null, null, null, null, null
    )
}
