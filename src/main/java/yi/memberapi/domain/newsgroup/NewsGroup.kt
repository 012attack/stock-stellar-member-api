package yi.memberapi.domain.newsgroup

import jakarta.persistence.*
import yi.memberapi.domain.member.Member
import yi.memberapi.domain.news.News
import java.time.LocalDateTime

@Entity
@Table(name = "news_groups")
class NewsGroup(
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
        name = "news_group_news",
        joinColumns = [JoinColumn(name = "news_group_id")],
        inverseJoinColumns = [JoinColumn(name = "news_id")]
    )
    val news: MutableSet<News> = mutableSetOf()
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
