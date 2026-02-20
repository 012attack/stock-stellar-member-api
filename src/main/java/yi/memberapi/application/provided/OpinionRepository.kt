package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.opinion.Opinion
import yi.memberapi.domain.opinion.TargetType

interface OpinionRepository : JpaRepository<Opinion, Int> {

    @Query("""
        SELECT o FROM Opinion o
        LEFT JOIN FETCH o.member
        WHERE o.targetType = :targetType AND o.targetId = :targetId
        ORDER BY o.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(o) FROM Opinion o
        WHERE o.targetType = :targetType AND o.targetId = :targetId
    """)
    fun findByTargetTypeAndTargetId(
        targetType: TargetType,
        targetId: Int,
        pageable: Pageable
    ): Page<Opinion>

    @Query("""
        SELECT o FROM Opinion o
        LEFT JOIN FETCH o.member
        WHERE o.targetType = :targetType
        ORDER BY o.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(o) FROM Opinion o
        WHERE o.targetType = :targetType
    """)
    fun findByTargetType(
        targetType: TargetType,
        pageable: Pageable
    ): Page<Opinion>

    @Query("""
        SELECT o FROM Opinion o
        LEFT JOIN FETCH o.member
        WHERE o.id IN :favoriteIds AND o.targetType = :targetType AND o.targetId = :targetId
        ORDER BY o.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(o) FROM Opinion o
        WHERE o.id IN :favoriteIds AND o.targetType = :targetType AND o.targetId = :targetId
    """)
    fun findByTargetTypeAndTargetIdByFavoriteIds(
        targetType: TargetType,
        targetId: Int,
        favoriteIds: List<Int>,
        pageable: Pageable
    ): Page<Opinion>

    @Query("""
        SELECT o FROM Opinion o
        LEFT JOIN FETCH o.member
        WHERE o.id IN :favoriteIds AND o.targetType = :targetType
        ORDER BY o.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(o) FROM Opinion o
        WHERE o.id IN :favoriteIds AND o.targetType = :targetType
    """)
    fun findByTargetTypeByFavoriteIds(
        targetType: TargetType,
        favoriteIds: List<Int>,
        pageable: Pageable
    ): Page<Opinion>
}
