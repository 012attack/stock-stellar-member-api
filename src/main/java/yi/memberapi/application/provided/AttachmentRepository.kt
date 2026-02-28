package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.attachment.Attachment

interface AttachmentRepository : JpaRepository<Attachment, Int> {

    @Query("""
        SELECT a FROM Attachment a
        LEFT JOIN FETCH a.member
        WHERE a.member.id = :memberId
        ORDER BY a.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(a) FROM Attachment a
        WHERE a.member.id = :memberId
    """)
    fun findByMemberId(
        memberId: Long,
        pageable: Pageable
    ): Page<Attachment>

    @Query("SELECT a FROM Attachment a LEFT JOIN FETCH a.member WHERE a.id = :id")
    fun findByIdWithMember(id: Int): Attachment?
}
