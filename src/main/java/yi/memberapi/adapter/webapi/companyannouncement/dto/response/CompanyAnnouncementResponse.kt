package yi.memberapi.adapter.webapi.companyannouncement.dto.response

import yi.memberapi.domain.companyannouncement.AnnouncementType
import java.time.LocalDate
import java.time.LocalDateTime

data class CompanyAnnouncementResponse(
    val id: Int,
    val stockId: Int,
    val stockName: String,
    val stockCode: String,
    val rceptNo: String,
    val corpCode: String?,
    val reportNm: String,
    val flrNm: String?,
    val rceptDt: LocalDate,
    val pblntfTy: AnnouncementType?,
    val pblntfDetailTy: String?,
    val createdAt: LocalDateTime?
)
