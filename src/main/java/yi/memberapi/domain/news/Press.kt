package yi.memberapi.domain.news

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "press")
class Press(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "name", nullable = false, length = 100)
    val name: String,

    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null
) {
    protected constructor() : this(null, "", null)
}
