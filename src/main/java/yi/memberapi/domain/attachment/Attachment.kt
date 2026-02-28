package yi.memberapi.domain.attachment

import jakarta.persistence.*
import yi.memberapi.domain.member.Member
import java.time.LocalDateTime

@Entity
@Table(name = "attachments")
class Attachment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "original_file_name", nullable = false, length = 500)
    val originalFileName: String,

    @Column(name = "stored_file_name", nullable = false, length = 500)
    val storedFileName: String,

    @Column(name = "file_path", nullable = false, length = 1000)
    val filePath: String,

    @Column(name = "file_size", nullable = false)
    val fileSize: Long,

    @Column(name = "content_type", length = 200)
    val contentType: String?,

    @Enumerated(EnumType.STRING)
    @Column(name = "file_type", nullable = false, length = 20)
    val fileType: FileType,

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

    protected constructor() : this(
        null, "", "", "", 0, null,
        FileType.OTHER, null, null, null
    )
}
