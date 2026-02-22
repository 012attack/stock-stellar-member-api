package yi.memberapi.domain.schedule

import jakarta.persistence.*
import yi.memberapi.domain.member.Member
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "stock_market_schedules")
class StockMarketSchedule(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "title", nullable = false, length = 200)
    var title: String,

    @Column(name = "content", columnDefinition = "TEXT")
    var content: String? = null,

    @Column(name = "schedule_date", nullable = false)
    var scheduleDate: LocalDate,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member? = null,

    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null,

    @Column(name = "updated_at")
    var updatedAt: LocalDateTime? = null
) {
    @PrePersist
    fun prePersist() {
        val now = LocalDateTime.now()
        if (createdAt == null) createdAt = now
        if (updatedAt == null) updatedAt = now
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
