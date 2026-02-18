package yi.memberapi.adapter.webapi.record

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.webapi.record.dto.response.DailyTop30RecordListResponse
import yi.memberapi.application.required.DailyTop30RecordLister
import java.time.LocalDate

@RestController
@RequestMapping("/api/daily-top30-records")
class GetDailyTop30RecordListApi(
    private val dailyTop30RecordLister: DailyTop30RecordLister
) {

    @GetMapping
    fun getDailyTop30Records(
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startDate: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) endDate: LocalDate?,
        @RequestParam(required = false) stockName: String?,
        @RequestParam(required = false) stockCode: String?,
        @RequestParam(required = false) themeName: String?
    ): ResponseEntity<DailyTop30RecordListResponse> {
        val response = dailyTop30RecordLister.listByDateRange(startDate, endDate, stockName, stockCode, themeName)
        return ResponseEntity.ok(response)
    }
}
