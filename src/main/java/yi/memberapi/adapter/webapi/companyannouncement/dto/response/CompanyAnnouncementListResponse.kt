package yi.memberapi.adapter.webapi.companyannouncement.dto.response

data class CompanyAnnouncementListResponse(
    val announcements: List<CompanyAnnouncementResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
