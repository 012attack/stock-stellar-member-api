package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.response.DailyTop30RecordListResponse
import java.time.LocalDate

interface DailyTop30RecordLister {
    fun listByDate(recordDate: LocalDate?): DailyTop30RecordListResponse
}
