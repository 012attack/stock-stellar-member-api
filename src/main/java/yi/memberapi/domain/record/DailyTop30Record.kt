package yi.memberapi.domain.record

import jakarta.persistence.*
import yi.memberapi.domain.stock.Stock
import yi.memberapi.domain.theme.Theme
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "daily_top30_records")
class DailyTop30Record(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "record_date", nullable = false)
    val recordDate: LocalDate,

    @Column(name = "rank", nullable = false)
    val rank: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    val stock: Stock? = null,

    @Column(name = "change_rate", length = 20)
    val changeRate: String? = null,

    @Column(name = "description", columnDefinition = "TEXT")
    val description: String? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "daily_record_themes",
        joinColumns = [JoinColumn(name = "daily_record_id")],
        inverseJoinColumns = [JoinColumn(name = "theme_id")]
    )
    val themes: MutableSet<Theme> = mutableSetOf()
) {
    protected constructor() : this(null, LocalDate.now(), 0, null, null, null, null, mutableSetOf())
}
