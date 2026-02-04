package yi.memberapi.domain.theme

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "themes")
class Theme(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "theme_name", nullable = false, length = 200)
    val themeName: String,

    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null
) {
    protected constructor() : this(null, "", null)
}
