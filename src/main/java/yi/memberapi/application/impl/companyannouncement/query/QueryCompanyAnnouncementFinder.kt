package yi.memberapi.application.impl.companyannouncement.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.companyannouncement.dto.response.CompanyAnnouncementResponse
import yi.memberapi.application.provided.CompanyAnnouncementRepository
import yi.memberapi.application.required.companyannouncement.CompanyAnnouncementFinder

@Service
@Transactional(readOnly = true)
class QueryCompanyAnnouncementFinder(
    private val companyAnnouncementRepository: CompanyAnnouncementRepository
) : CompanyAnnouncementFinder {

    override fun findById(id: Int): CompanyAnnouncementResponse? {
        return companyAnnouncementRepository.findByIdWithStock(id)?.let { announcement ->
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
    }
}
