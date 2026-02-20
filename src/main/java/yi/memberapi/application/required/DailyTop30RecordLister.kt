package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.record.dto.response.DailyTop30RecordListResponse
import java.time.LocalDate

interface DailyTop30RecordLister {
    fun listByDateRange(
        startDate: LocalDate,
        endDate: LocalDate,
        stockName: String? = null,
        stockCode: String? = null,
        themeName: String? = null,
        favoriteOnly: Boolean = false,
        memberId: Long? = null
    ): DailyTop30RecordListResponse

    fun listByPage(
        page: Int,
        size: Int,
        stockName: String? = null,
        stockCode: String? = null,
        themeName: String? = null,
        favoriteOnly: Boolean = false,
        memberId: Long? = null
    ): DailyTop30RecordListResponse
}
