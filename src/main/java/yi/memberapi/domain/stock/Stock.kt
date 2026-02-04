package yi.memberapi.domain.stock

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "stocks")
class Stock(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "stock_code", nullable = false, length = 10)
    val stockCode: String,

    @Column(name = "stock_name", nullable = false, length = 100)
    val stockName: String,

    @Column(name = "company_summary", columnDefinition = "TEXT")
    val companySummary: String? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null,

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime? = null
) {
    protected constructor() : this(null, "", "", null, null, null)
}
