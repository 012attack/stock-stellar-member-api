package yi.memberapi.adapter.webapi.companyannouncement

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.webapi.companyannouncement.dto.response.CompanyAnnouncementListResponse
import yi.memberapi.application.required.companyannouncement.CompanyAnnouncementLister

@RestController
@RequestMapping("/api/company-announcements")
class GetCompanyAnnouncementListApi(
    private val companyAnnouncementLister: CompanyAnnouncementLister
) {

    @GetMapping
    fun getCompanyAnnouncements(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) stockId: Int?,
        @RequestParam(required = false) reportNm: String?,
        @RequestParam(required = false) pblntfTy: String?
    ): ResponseEntity<CompanyAnnouncementListResponse> {
        val response = companyAnnouncementLister.list(page, size, stockId, reportNm, pblntfTy)
        return ResponseEntity.ok(response)
    }
}
