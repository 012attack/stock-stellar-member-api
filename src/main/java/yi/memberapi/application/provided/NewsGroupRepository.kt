package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.newsgroup.NewsGroup

interface NewsGroupRepository : JpaRepository<NewsGroup, Int> {

    @Query("""
        SELECT ng FROM NewsGroup ng
        WHERE ng.member.id = :memberId
        ORDER BY ng.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(ng) FROM NewsGroup ng
        WHERE ng.member.id = :memberId
    """)
    fun findByMemberId(memberId: Long, pageable: Pageable): Page<NewsGroup>

    @Query("""
        SELECT ng FROM NewsGroup ng
        WHERE ng.member.id = :memberId AND ng.id IN :favoriteIds
        ORDER BY ng.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(ng) FROM NewsGroup ng
        WHERE ng.member.id = :memberId AND ng.id IN :favoriteIds
    """)
    fun findByMemberIdAndFavoriteIds(memberId: Long, favoriteIds: List<Int>, pageable: Pageable): Page<NewsGroup>

    @Query("SELECT ng FROM NewsGroup ng LEFT JOIN FETCH ng.news WHERE ng.id = :id")
    fun findByIdWithNews(id: Int): NewsGroup?

    @Query("SELECT ng FROM NewsGroup ng LEFT JOIN FETCH ng.member WHERE ng.id = :id")
    fun findByIdWithMember(id: Int): NewsGroup?
}
