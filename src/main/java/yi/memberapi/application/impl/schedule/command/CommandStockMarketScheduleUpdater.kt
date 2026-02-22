package yi.memberapi.application.impl.schedule.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.schedule.dto.request.UpdateStockMarketScheduleRequest
import yi.memberapi.adapter.webapi.schedule.dto.response.StockMarketScheduleResponse
import yi.memberapi.application.required.StockMarketScheduleFinder
import yi.memberapi.application.required.StockMarketScheduleUpdater

@Service
@Transactional
class CommandStockMarketScheduleUpdater(
    private val stockMarketScheduleFinder: StockMarketScheduleFinder
) : StockMarketScheduleUpdater {

    override fun update(id: Int, request: UpdateStockMarketScheduleRequest, memberId: Long): StockMarketScheduleResponse {
        val schedule = stockMarketScheduleFinder.findEntityByIdWithMember(id)

        if (schedule.member?.id != memberId) {
            throw IllegalArgumentException("Not authorized to update this schedule")
        }

        schedule.title = request.title
        schedule.content = request.content
        schedule.scheduleDate = request.scheduleDate

        return StockMarketScheduleResponse(
            id = schedule.id!!,
            title = schedule.title,
            content = schedule.content,
            scheduleDate = schedule.scheduleDate,
            createdAt = schedule.createdAt,
            updatedAt = schedule.updatedAt
        )
    }
}
