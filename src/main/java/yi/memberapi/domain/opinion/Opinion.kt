package yi.memberapi.domain.opinion

import jakarta.persistence.*
import yi.memberapi.domain.member.Member
import java.time.LocalDateTime

@Entity
@Table(name = "opinions")
class Opinion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "title", nullable = false, length = 200)
    val title: String,

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    val content: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false, length = 20)
    val targetType: TargetType,

    @Column(name = "target_id", nullable = false)
    val targetId: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null
) {
    protected constructor() : this(null, "", "", TargetType.RECORD, 0, null, null)
}
