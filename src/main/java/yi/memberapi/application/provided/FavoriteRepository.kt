package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.favorite.Favorite
import yi.memberapi.domain.favorite.FavoriteTargetType

interface FavoriteRepository : JpaRepository<Favorite, Int> {

    @Query("""
        SELECT f FROM Favorite f
        LEFT JOIN FETCH f.member
        WHERE f.member.id = :memberId AND f.targetType = :targetType
        ORDER BY f.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(f) FROM Favorite f
        WHERE f.member.id = :memberId AND f.targetType = :targetType
    """)
    fun findByMemberIdAndTargetType(
        memberId: Long,
        targetType: FavoriteTargetType,
        pageable: Pageable
    ): Page<Favorite>

    fun existsByMemberIdAndTargetTypeAndTargetId(
        memberId: Long,
        targetType: FavoriteTargetType,
        targetId: Int
    ): Boolean

    fun deleteByMemberIdAndTargetTypeAndTargetId(
        memberId: Long,
        targetType: FavoriteTargetType,
        targetId: Int
    )
}
