package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.news.News

interface NewsRepository : JpaRepository<News, Int> {

    @Query("""
        SELECT DISTINCT n FROM News n
        LEFT JOIN FETCH n.press
        LEFT JOIN n.themes t
        WHERE (:title IS NULL OR n.title LIKE %:title%)
        AND (:pressName IS NULL OR n.press.name LIKE %:pressName%)
        AND (:themeName IS NULL OR t.themeName LIKE %:themeName%)
        ORDER BY n.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(DISTINCT n) FROM News n
        LEFT JOIN n.themes t
        WHERE (:title IS NULL OR n.title LIKE %:title%)
        AND (:pressName IS NULL OR n.press.name LIKE %:pressName%)
        AND (:themeName IS NULL OR t.themeName LIKE %:themeName%)
    """)
    fun findWithFilters(
        title: String?,
        pressName: String?,
        themeName: String?,
        pageable: Pageable
    ): Page<News>

    @Query("SELECT n FROM News n LEFT JOIN FETCH n.press ORDER BY n.createdAt DESC")
    fun findAllWithPress(pageable: Pageable): Page<News>

    @Query("SELECT n FROM News n LEFT JOIN FETCH n.press WHERE n.id = :id")
    fun findByIdWithPress(id: Int): News?

    @Query("SELECT n FROM News n LEFT JOIN FETCH n.press LEFT JOIN FETCH n.themes WHERE n.id = :id")
    fun findByIdWithPressAndThemes(id: Int): News?
}
