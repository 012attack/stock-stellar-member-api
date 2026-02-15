package yi.memberapi.domain.news

import jakarta.persistence.*
import yi.memberapi.domain.theme.Theme
import java.time.LocalDateTime

@Entity
@Table(name = "news")
class News(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "title", nullable = false, columnDefinition = "TEXT")
    val title: String,

    @Column(name = "link", nullable = false, length = 500)
    val link: String,

    @Column(name = "source_file", length = 200)
    val sourceFile: String? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "press_id")
    val press: Press? = null,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "news_themes",
        joinColumns = [JoinColumn(name = "news_id")],
        inverseJoinColumns = [JoinColumn(name = "theme_id")]
    )
    val themes: MutableSet<Theme> = mutableSetOf()
) {
    protected constructor() : this(null, "", "", null, null, null, mutableSetOf())
}
