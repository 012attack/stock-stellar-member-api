package yi.memberapi.application.impl.record.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.record.dto.response.DailyTop30RecordListResponse
import yi.memberapi.adapter.webapi.record.dto.response.DailyTop30RecordResponse
import yi.memberapi.adapter.webapi.stock.dto.response.StockResponse
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeResponse
import yi.memberapi.application.provided.DailyTop30RecordRepository
import yi.memberapi.application.provided.FavoriteRepository
import yi.memberapi.application.provided.ImportanceRepository
import yi.memberapi.application.required.DailyTop30RecordLister
import yi.memberapi.domain.favorite.FavoriteTargetType
import yi.memberapi.domain.importance.ImportanceTargetType
import yi.memberapi.domain.record.DailyTop30Record
import java.math.BigDecimal
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class QueryDailyTop30RecordLister(
    private val dailyTop30RecordRepository: DailyTop30RecordRepository,
    private val favoriteRepository: FavoriteRepository,
    private val importanceRepository: ImportanceRepository
) : DailyTop30RecordLister {

    private fun resolveFilteredIds(favoriteOnly: Boolean, memberId: Long?, minScore: BigDecimal?): List<Int>? {
        val favoriteIds = if (favoriteOnly && memberId != null) {
            favoriteRepository.findTargetIdsByMemberIdAndTargetType(memberId, FavoriteTargetType.RECORD)
        } else null

        val importanceIds = if (minScore != null && memberId != null) {
            importanceRepository.findTargetIdsByMemberIdAndTargetTypeAndMinScore(memberId, ImportanceTargetType.RECORD, minScore)
        } else null

        return when {
            favoriteIds != null && importanceIds != null -> favoriteIds.intersect(importanceIds.toSet()).toList()
            favoriteIds != null -> favoriteIds
            importanceIds != null -> importanceIds
            else -> null
        }
    }

    override fun listByDateRange(
        startDate: LocalDate,
        endDate: LocalDate,
        stockName: String?,
        stockCode: String?,
        themeName: String?,
        favoriteOnly: Boolean,
        memberId: Long?,
        minScore: BigDecimal?
    ): DailyTop30RecordListResponse {
        val filteredIds = resolveFilteredIds(favoriteOnly, memberId, minScore)

        if (filteredIds != null) {
            if (filteredIds.isEmpty()) return DailyTop30RecordListResponse(records = emptyList(), startDate = startDate, endDate = endDate)

            val hasFilters = stockName != null || stockCode != null || themeName != null
            val records = if (hasFilters) {
                dailyTop30RecordRepository.findByDateRangeWithFiltersByFavoriteIds(startDate, endDate, stockName, stockCode, themeName, filteredIds)
            } else {
                dailyTop30RecordRepository.findByRecordDateBetweenWithStockAndThemesByFavoriteIds(startDate, endDate, filteredIds)
            }

            return DailyTop30RecordListResponse(
                records = records.map { it.toResponse() },
                startDate = startDate,
                endDate = endDate
            )
        }

        val hasFilters = stockName != null || stockCode != null || themeName != null
        val records = if (hasFilters) {
            dailyTop30RecordRepository.findByDateRangeWithFilters(startDate, endDate, stockName, stockCode, themeName)
        } else {
            dailyTop30RecordRepository.findByRecordDateBetweenWithStockAndThemes(startDate, endDate)
        }

        return DailyTop30RecordListResponse(
            records = records.map { it.toResponse() },
            startDate = startDate,
            endDate = endDate
        )
    }

    override fun listByPage(
        page: Int,
        size: Int,
        stockName: String?,
        stockCode: String?,
        themeName: String?,
        favoriteOnly: Boolean,
        memberId: Long?,
        minScore: BigDecimal?
    ): DailyTop30RecordListResponse {
        val pageable = PageRequest.of(page, size)
        val filteredIds = resolveFilteredIds(favoriteOnly, memberId, minScore)

        val recordPage = if (filteredIds != null) {
            if (filteredIds.isEmpty()) return DailyTop30RecordListResponse(
                records = emptyList(),
                page = page,
                size = size,
                totalElements = 0,
                totalPages = 0
            )

            val hasFilters = stockName != null || stockCode != null || themeName != null
            if (hasFilters) {
                dailyTop30RecordRepository.findWithFiltersByFavoriteIds(stockName, stockCode, themeName, filteredIds, pageable)
            } else {
                dailyTop30RecordRepository.findAllWithStockAndThemesByFavoriteIds(filteredIds, pageable)
            }
        } else {
            val hasFilters = stockName != null || stockCode != null || themeName != null
            if (hasFilters) {
                dailyTop30RecordRepository.findWithFilters(stockName, stockCode, themeName, pageable)
            } else {
                dailyTop30RecordRepository.findAllWithStockAndThemes(pageable)
            }
        }

        return DailyTop30RecordListResponse(
            records = recordPage.content.map { it.toResponse() },
            page = recordPage.number,
            size = recordPage.size,
            totalElements = recordPage.totalElements,
            totalPages = recordPage.totalPages
        )
    }

    private fun DailyTop30Record.toResponse(): DailyTop30RecordResponse {
        return DailyTop30RecordResponse(
            id = this.id!!,
            recordDate = this.recordDate,
            rank = this.rank,
            changeRate = this.changeRate,
            description = this.description,
            createdAt = this.createdAt,
            stock = this.stock?.let {
                StockResponse(
                    id = it.id!!,
                    stockCode = it.stockCode,
                    stockName = it.stockName,
                    companySummary = it.companySummary
                )
            },
            themes = this.themes.map { theme ->
                ThemeResponse(
                    id = theme.id!!,
                    themeName = theme.themeName
                )
            }
        )
    }
}
