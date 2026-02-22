package yi.memberapi.application.impl.schedule.query

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.schedule.dto.response.StockMarketScheduleListResponse
import yi.memberapi.adapter.webapi.schedule.dto.response.StockMarketScheduleResponse
import yi.memberapi.application.provided.StockMarketScheduleRepository
import yi.memberapi.application.required.StockMarketScheduleLister
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class QueryStockMarketScheduleLister(
    private val stockMarketScheduleRepository: StockMarketScheduleRepository
) : StockMarketScheduleLister {

    override fun list(page: Int, size: Int, startDate: LocalDate?, endDate: LocalDate?): StockMarketScheduleListResponse {
        val pageable = PageRequest.of(page, size)

        val schedulePage = if (startDate != null && endDate != null) {
            stockMarketScheduleRepository.findByScheduleDateBetween(startDate, endDate, pageable)
        } else {
            stockMarketScheduleRepository.findAllOrderByScheduleDateDesc(pageable)
        }

        val scheduleList = schedulePage.content.map { schedule ->
            StockMarketScheduleResponse(
                id = schedule.id!!,
                title = schedule.title,
                content = schedule.content,
                scheduleDate = schedule.scheduleDate,
                createdAt = schedule.createdAt,
                updatedAt = schedule.updatedAt
            )
        }

        return StockMarketScheduleListResponse(
            schedules = scheduleList,
            page = schedulePage.number,
            size = schedulePage.size,
            totalElements = schedulePage.totalElements,
            totalPages = schedulePage.totalPages
        )
    }
}
