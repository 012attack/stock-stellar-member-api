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
import yi.memberapi.application.required.DailyTop30RecordLister
import yi.memberapi.domain.favorite.FavoriteTargetType
import yi.memberapi.domain.record.DailyTop30Record
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class QueryDailyTop30RecordLister(
    private val dailyTop30RecordRepository: DailyTop30RecordRepository,
    private val favoriteRepository: FavoriteRepository
) : DailyTop30RecordLister {

    override fun listByDateRange(
        startDate: LocalDate,
        endDate: LocalDate,
        stockName: String?,
        stockCode: String?,
        themeName: String?,
        favoriteOnly: Boolean,
        memberId: Long?
    ): DailyTop30RecordListResponse {
        if (favoriteOnly && memberId != null) {
            val favoriteIds = favoriteRepository.findTargetIdsByMemberIdAndTargetType(memberId, FavoriteTargetType.RECORD)
            if (favoriteIds.isEmpty()) return DailyTop30RecordListResponse(records = emptyList(), startDate = startDate, endDate = endDate)

            val hasFilters = stockName != null || stockCode != null || themeName != null
            val records = if (hasFilters) {
                dailyTop30RecordRepository.findByDateRangeWithFiltersByFavoriteIds(startDate, endDate, stockName, stockCode, themeName, favoriteIds)
            } else {
                dailyTop30RecordRepository.findByRecordDateBetweenWithStockAndThemesByFavoriteIds(startDate, endDate, favoriteIds)
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
        memberId: Long?
    ): DailyTop30RecordListResponse {
        val pageable = PageRequest.of(page, size)

        val recordPage = if (favoriteOnly && memberId != null) {
            val favoriteIds = favoriteRepository.findTargetIdsByMemberIdAndTargetType(memberId, FavoriteTargetType.RECORD)
            if (favoriteIds.isEmpty()) return DailyTop30RecordListResponse(
                records = emptyList(),
                page = page,
                size = size,
                totalElements = 0,
                totalPages = 0
            )

            val hasFilters = stockName != null || stockCode != null || themeName != null
            if (hasFilters) {
                dailyTop30RecordRepository.findWithFiltersByFavoriteIds(stockName, stockCode, themeName, favoriteIds, pageable)
            } else {
                dailyTop30RecordRepository.findAllWithStockAndThemesByFavoriteIds(favoriteIds, pageable)
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
