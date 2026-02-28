package yi.memberapi.adapter.webapi.companyannouncement

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.webapi.companyannouncement.dto.response.CompanyAnnouncementResponse
import yi.memberapi.application.required.companyannouncement.CompanyAnnouncementFinder

@RestController
@RequestMapping("/api/company-announcements")
class GetCompanyAnnouncementDetailApi(
    private val companyAnnouncementFinder: CompanyAnnouncementFinder
) {

    @GetMapping("/{id}")
    fun getCompanyAnnouncementDetail(@PathVariable id: Int): ResponseEntity<CompanyAnnouncementResponse> {
        val announcement = companyAnnouncementFinder.findById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(announcement)
    }
}
