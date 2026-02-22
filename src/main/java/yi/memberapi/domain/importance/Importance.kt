package yi.memberapi.domain.importance

import jakarta.persistence.*
import yi.memberapi.domain.member.Member
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "importances")
class Importance(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    val targetType: ImportanceTargetType,

    @Column(name = "target_id", nullable = false)
    val targetId: Int,

    @Column(name = "score", nullable = false, precision = 3, scale = 1)
    var score: BigDecimal,

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

    protected constructor() : this(null, ImportanceTargetType.RECORD, 0, BigDecimal.ZERO, null, null, null)
}
