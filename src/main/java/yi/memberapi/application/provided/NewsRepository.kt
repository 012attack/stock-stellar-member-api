package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.news.News

interface NewsRepository : JpaRepository<News, Int> {

    @Query("""
        SELECT n FROM News n
        LEFT JOIN FETCH n.press
        WHERE (:title IS NULL OR n.title LIKE %:title%)
        AND (:pressId IS NULL OR n.press.id = :pressId)
        ORDER BY n.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(n) FROM News n
        WHERE (:title IS NULL OR n.title LIKE %:title%)
        AND (:pressId IS NULL OR n.press.id = :pressId)
    """)
    fun findWithFilters(
        title: String?,
        pressId: Int?,
        pageable: Pageable
    ): Page<News>

    @Query("SELECT n FROM News n LEFT JOIN FETCH n.press ORDER BY n.createdAt DESC")
    fun findAllWithPress(pageable: Pageable): Page<News>

    @Query("SELECT n FROM News n LEFT JOIN FETCH n.press WHERE n.id = :id")
    fun findByIdWithPress(id: Int): News?
}
