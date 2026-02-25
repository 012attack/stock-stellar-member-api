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
import yi.memberapi.application.provided.ReadCheckRepository
import yi.memberapi.application.required.DailyTop30RecordLister
import yi.memberapi.domain.favorite.FavoriteTargetType
import yi.memberapi.domain.importance.ImportanceTargetType
import yi.memberapi.domain.readcheck.ReadCheckTargetType
import yi.memberapi.domain.readcheck.ReadFilter
import yi.memberapi.domain.record.DailyTop30Record
import java.math.BigDecimal
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class QueryDailyTop30RecordLister(
    private val dailyTop30RecordRepository: DailyTop30RecordRepository,
    private val favoriteRepository: FavoriteRepository,
    private val importanceRepository: ImportanceRepository,
    private val readCheckRepository: ReadCheckRepository
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

    private fun applyReadFilter(filteredIds: List<Int>?, readFilter: ReadFilter?, memberId: Long?): Pair<List<Int>?, List<Int>?> {
        if (readFilter == null || memberId == null) return Pair(filteredIds, null)

        val readIds = readCheckRepository.findTargetIdsByMemberIdAndTargetType(memberId, ReadCheckTargetType.RECORD)

        return when (readFilter) {
            ReadFilter.READ -> {
                val newFilteredIds = if (filteredIds != null) {
                    filteredIds.intersect(readIds.toSet()).toList()
                } else {
                    readIds
                }
                Pair(newFilteredIds, null)
            }
            ReadFilter.UNREAD -> {
                if (filteredIds != null) {
                    Pair(filteredIds.minus(readIds.toSet()), null)
                } else {
                    Pair(null, readIds)
                }
            }
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
        minScore: BigDecimal?,
        readFilter: ReadFilter?
    ): DailyTop30RecordListResponse {
        val baseFilteredIds = resolveFilteredIds(favoriteOnly, memberId, minScore)
        val (filteredIds, excludeIds) = applyReadFilter(baseFilteredIds, readFilter, memberId)

        val emptyResponse = DailyTop30RecordListResponse(records = emptyList(), startDate = startDate, endDate = endDate)
        val hasFilters = stockName != null || stockCode != null || themeName != null

        if (filteredIds != null) {
            if (filteredIds.isEmpty()) return emptyResponse

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

        if (excludeIds != null) {
            if (excludeIds.isEmpty()) {
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

            val records = if (hasFilters) {
                dailyTop30RecordRepository.findByDateRangeWithFiltersByExcludeIds(startDate, endDate, stockName, stockCode, themeName, excludeIds)
            } else {
                dailyTop30RecordRepository.findByRecordDateBetweenWithStockAndThemesByExcludeIds(startDate, endDate, excludeIds)
            }

            return DailyTop30RecordListResponse(
                records = records.map { it.toResponse() },
                startDate = startDate,
                endDate = endDate
            )
        }

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
        minScore: BigDecimal?,
        readFilter: ReadFilter?
    ): DailyTop30RecordListResponse {
        val pageable = PageRequest.of(page, size)
        val baseFilteredIds = resolveFilteredIds(favoriteOnly, memberId, minScore)
        val (filteredIds, excludeIds) = applyReadFilter(baseFilteredIds, readFilter, memberId)

        val emptyResponse = DailyTop30RecordListResponse(
            records = emptyList(),
            page = page,
            size = size,
            totalElements = 0,
            totalPages = 0
        )
        val hasFilters = stockName != null || stockCode != null || themeName != null

        val recordPage = if (filteredIds != null) {
            if (filteredIds.isEmpty()) return emptyResponse

            if (hasFilters) {
                dailyTop30RecordRepository.findWithFiltersByFavoriteIds(stockName, stockCode, themeName, filteredIds, pageable)
            } else {
                dailyTop30RecordRepository.findAllWithStockAndThemesByFavoriteIds(filteredIds, pageable)
            }
        } else if (excludeIds != null) {
            if (excludeIds.isEmpty()) {
                if (hasFilters) {
                    dailyTop30RecordRepository.findWithFilters(stockName, stockCode, themeName, pageable)
                } else {
                    dailyTop30RecordRepository.findAllWithStockAndThemes(pageable)
                }
            } else {
                if (hasFilters) {
                    dailyTop30RecordRepository.findWithFiltersByExcludeIds(stockName, stockCode, themeName, excludeIds, pageable)
                } else {
                    dailyTop30RecordRepository.findAllWithStockAndThemesByExcludeIds(excludeIds, pageable)
                }
            }
        } else {
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
