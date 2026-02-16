package yi.memberapi.domain.stockgroup

import jakarta.persistence.*
import yi.memberapi.domain.member.Member
import yi.memberapi.domain.stock.Stock
import java.time.LocalDateTime

@Entity
@Table(name = "stock_groups")
class StockGroup(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "title", nullable = false, length = 200)
    var title: String,

    @Column(name = "description", columnDefinition = "TEXT")
    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member? = null,

    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "stock_group_stocks",
        joinColumns = [JoinColumn(name = "stock_group_id")],
        inverseJoinColumns = [JoinColumn(name = "stock_id")]
    )
    val stocks: MutableSet<Stock> = mutableSetOf()
) {
    @PrePersist
    fun prePersist() {
        val now = LocalDateTime.now()
        if (createdAt == null) {
            createdAt = now
        }
        if (updatedAt == null) {
            updatedAt = now
        }
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }

    protected constructor() : this(null, "", null, null, null, null, mutableSetOf())
}
