package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.theme.Theme

interface ThemeRepository : JpaRepository<Theme, Int> {

    @Query("""
        SELECT t FROM Theme t
        WHERE (:themeName IS NULL OR t.themeName LIKE %:themeName%)
        ORDER BY t.themeName ASC
    """,
        countQuery = """
        SELECT COUNT(t) FROM Theme t
        WHERE (:themeName IS NULL OR t.themeName LIKE %:themeName%)
    """)
    fun findWithFilters(
        themeName: String?,
        pageable: Pageable
    ): Page<Theme>

    @Query("""
        SELECT t FROM Theme t
        WHERE t.id IN :favoriteIds
        AND (:themeName IS NULL OR t.themeName LIKE %:themeName%)
        ORDER BY t.themeName ASC
    """,
        countQuery = """
        SELECT COUNT(t) FROM Theme t
        WHERE t.id IN :favoriteIds
        AND (:themeName IS NULL OR t.themeName LIKE %:themeName%)
    """)
    fun findWithFiltersByFavoriteIds(
        themeName: String?,
        favoriteIds: List<Int>,
        pageable: Pageable
    ): Page<Theme>
}
