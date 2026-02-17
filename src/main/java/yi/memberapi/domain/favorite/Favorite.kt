package yi.memberapi.domain.favorite

import jakarta.persistence.*
import yi.memberapi.domain.member.Member
import java.time.LocalDateTime

@Entity
@Table(name = "favorites")
class Favorite(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    val targetType: FavoriteTargetType,

    @Column(name = "target_id", nullable = false)
    val targetId: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member? = null,

    @Column(name = "created_at")
    var createdAt: LocalDateTime? = null
) {
    @PrePersist
    fun prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now()
        }
    }

    protected constructor() : this(null, FavoriteTargetType.STOCK, 0, null, null)
}
