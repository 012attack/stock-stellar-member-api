package yi.memberapi.application.required.companyannouncement

import yi.memberapi.adapter.webapi.companyannouncement.dto.response.CompanyAnnouncementResponse

interface CompanyAnnouncementFinder {
    fun findById(id: Int): CompanyAnnouncementResponse?
}
