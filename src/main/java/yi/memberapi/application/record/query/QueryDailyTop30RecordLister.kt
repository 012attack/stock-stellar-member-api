package yi.memberapi.application.record.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.response.DailyTop30RecordListResponse
import yi.memberapi.adapter.webapi.dto.response.DailyTop30RecordResponse
import yi.memberapi.adapter.webapi.dto.response.StockResponse
import yi.memberapi.adapter.webapi.dto.response.ThemeResponse
import yi.memberapi.application.provided.DailyTop30RecordRepository
import yi.memberapi.application.required.DailyTop30RecordLister
import yi.memberapi.domain.record.DailyTop30Record
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class QueryDailyTop30RecordLister(
    private val dailyTop30RecordRepository: DailyTop30RecordRepository
) : DailyTop30RecordLister {

    override fun listByDate(recordDate: LocalDate?): DailyTop30RecordListResponse {
        val targetDate = recordDate ?: getLatestRecordDate()

        val records = dailyTop30RecordRepository.findByRecordDateWithStockAndThemes(targetDate)

        return DailyTop30RecordListResponse(
            records = records.map { it.toResponse() },
            recordDate = targetDate
        )
    }

    private fun getLatestRecordDate(): LocalDate {
        val dates = dailyTop30RecordRepository.findDistinctRecordDates(PageRequest.of(0, 1))
        return dates.firstOrNull() ?: LocalDate.now()
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
