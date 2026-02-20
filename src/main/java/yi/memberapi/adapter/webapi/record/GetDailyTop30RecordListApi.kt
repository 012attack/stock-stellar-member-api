package yi.memberapi.adapter.webapi.record

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
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
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) stockName: String?,
        @RequestParam(required = false) stockCode: String?,
        @RequestParam(required = false) themeName: String?,
        @RequestParam(required = false) favoriteOnly: Boolean = false
    ): ResponseEntity<DailyTop30RecordListResponse> {
        val memberId = if (favoriteOnly) {
            val memberUserDetails = SecurityContextHolder.getContext().authentication?.principal as? MemberUserDetails
            memberUserDetails?.getMember()?.id
        } else null

        val response = if (startDate != null && endDate != null) {
            dailyTop30RecordLister.listByDateRange(startDate, endDate, stockName, stockCode, themeName, favoriteOnly, memberId)
        } else {
            dailyTop30RecordLister.listByPage(page, size, stockName, stockCode, themeName, favoriteOnly, memberId)
        }

        return ResponseEntity.ok(response)
    }
}
