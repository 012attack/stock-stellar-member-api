package yi.memberapi.application.impl.companyannouncement.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.companyannouncement.dto.response.CompanyAnnouncementListResponse
import yi.memberapi.adapter.webapi.companyannouncement.dto.response.CompanyAnnouncementResponse
import yi.memberapi.application.provided.CompanyAnnouncementRepository
import yi.memberapi.application.required.companyannouncement.CompanyAnnouncementLister

@Service
@Transactional(readOnly = true)
class QueryCompanyAnnouncementLister(
    private val companyAnnouncementRepository: CompanyAnnouncementRepository
) : CompanyAnnouncementLister {

    override fun list(page: Int, size: Int, stockId: Int?, reportNm: String?, pblntfTy: String?): CompanyAnnouncementListResponse {
        val pageable = PageRequest.of(page, size)

        val announcementPage = companyAnnouncementRepository.findWithFilters(stockId, reportNm, pblntfTy, pageable)

        val announcements = announcementPage.content.map { announcement ->
            CompanyAnnouncementResponse(
                id = announcement.id!!,
                stockId = announcement.stock.id!!,
                stockName = announcement.stock.stockName,
                stockCode = announcement.stock.stockCode,
                rceptNo = announcement.rceptNo,
                corpCode = announcement.corpCode,
                reportNm = announcement.reportNm,
                flrNm = announcement.flrNm,
                rceptDt = announcement.rceptDt,
                pblntfTy = announcement.pblntfTy,
                pblntfDetailTy = announcement.pblntfDetailTy,
                createdAt = announcement.createdAt
            )
        }

        return CompanyAnnouncementListResponse(
            announcements = announcements,
            page = announcementPage.number,
            size = announcementPage.size,
            totalElements = announcementPage.totalElements,
            totalPages = announcementPage.totalPages
        )
    }
}
