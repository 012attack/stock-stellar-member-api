package yi.memberapi.adapter.webapi.dto.response

import java.time.LocalDate

data class DailyTop30RecordListResponse(
    val records: List<DailyTop30RecordResponse>,
    val recordDate: LocalDate
)
