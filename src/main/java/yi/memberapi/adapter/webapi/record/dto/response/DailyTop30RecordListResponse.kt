package yi.memberapi.adapter.webapi.record.dto.response

import java.time.LocalDate

data class DailyTop30RecordListResponse(
    val records: List<DailyTop30RecordResponse>,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val page: Int? = null,
    val size: Int? = null,
    val totalElements: Long? = null,
    val totalPages: Int? = null
)
