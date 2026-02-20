package yi.memberapi.application.provided

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import yi.memberapi.domain.stockgroup.StockGroup

interface StockGroupRepository : JpaRepository<StockGroup, Int> {

    @Query("""
        SELECT sg FROM StockGroup sg
        WHERE sg.member.id = :memberId
        ORDER BY sg.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(sg) FROM StockGroup sg
        WHERE sg.member.id = :memberId
    """)
    fun findByMemberId(memberId: Long, pageable: Pageable): Page<StockGroup>

    @Query("""
        SELECT sg FROM StockGroup sg
        WHERE sg.member.id = :memberId AND sg.id IN :favoriteIds
        ORDER BY sg.createdAt DESC
    """,
        countQuery = """
        SELECT COUNT(sg) FROM StockGroup sg
        WHERE sg.member.id = :memberId AND sg.id IN :favoriteIds
    """)
    fun findByMemberIdAndFavoriteIds(memberId: Long, favoriteIds: List<Int>, pageable: Pageable): Page<StockGroup>

    @Query("SELECT sg FROM StockGroup sg LEFT JOIN FETCH sg.stocks WHERE sg.id = :id")
    fun findByIdWithStocks(id: Int): StockGroup?

    @Query("SELECT sg FROM StockGroup sg LEFT JOIN FETCH sg.member WHERE sg.id = :id")
    fun findByIdWithMember(id: Int): StockGroup?
}
