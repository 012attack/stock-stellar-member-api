package yi.memberapi.application.impl.schedule.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.schedule.dto.response.StockMarketScheduleDetailResponse
import yi.memberapi.application.provided.StockMarketScheduleRepository
import yi.memberapi.application.required.StockMarketScheduleFinder
import yi.memberapi.domain.schedule.StockMarketSchedule

@Service
@Transactional(readOnly = true)
class QueryStockMarketScheduleFinder(
    private val stockMarketScheduleRepository: StockMarketScheduleRepository
) : StockMarketScheduleFinder {

    override fun findEntityByIdWithMember(id: Int): StockMarketSchedule {
        return stockMarketScheduleRepository.findByIdWithMember(id)
            ?: throw IllegalArgumentException("StockMarketSchedule not found: $id")
    }

    override fun find(id: Int): StockMarketScheduleDetailResponse {
        val schedule = stockMarketScheduleRepository.findByIdWithMember(id)
            ?: throw IllegalArgumentException("StockMarketSchedule not found: $id")

        return StockMarketScheduleDetailResponse(
            id = schedule.id!!,
            title = schedule.title,
            content = schedule.content,
            scheduleDate = schedule.scheduleDate,
            memberName = schedule.member?.name,
            createdAt = schedule.createdAt,
            updatedAt = schedule.updatedAt
        )
    }
}
