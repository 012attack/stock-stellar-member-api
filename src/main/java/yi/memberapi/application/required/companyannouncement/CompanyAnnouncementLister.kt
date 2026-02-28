package yi.memberapi.application.required.companyannouncement

import yi.memberapi.adapter.webapi.companyannouncement.dto.response.CompanyAnnouncementListResponse

interface CompanyAnnouncementLister {
    fun list(page: Int, size: Int, stockId: Int?, reportNm: String?, pblntfTy: String?): CompanyAnnouncementListResponse
}
