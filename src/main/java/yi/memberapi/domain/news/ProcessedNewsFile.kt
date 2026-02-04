package yi.memberapi.domain.news

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "processed_news_files")
class ProcessedNewsFile(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "file_name", nullable = false, length = 300)
    val fileName: String,

    @Column(name = "news_count")
    val newsCount: Int? = 0,

    @Column(name = "inserted_count")
    val insertedCount: Int? = 0,

    @Column(name = "processed_at")
    val processedAt: LocalDateTime? = null
) {
    protected constructor() : this(null, "", 0, 0, null)
}
