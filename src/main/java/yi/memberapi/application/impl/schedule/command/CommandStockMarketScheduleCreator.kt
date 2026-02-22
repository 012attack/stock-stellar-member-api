package yi.memberapi.application.impl.schedule.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.schedule.dto.request.CreateStockMarketScheduleRequest
import yi.memberapi.adapter.webapi.schedule.dto.response.StockMarketScheduleResponse
import yi.memberapi.application.provided.StockMarketScheduleRepository
import yi.memberapi.application.required.MemberFinder
import yi.memberapi.application.required.StockMarketScheduleCreator
import yi.memberapi.domain.schedule.StockMarketSchedule

@Service
@Transactional
class CommandStockMarketScheduleCreator(
    private val stockMarketScheduleRepository: StockMarketScheduleRepository,
    private val memberFinder: MemberFinder
) : StockMarketScheduleCreator {

    override fun create(request: CreateStockMarketScheduleRequest, memberId: Long): StockMarketScheduleResponse {
        val member = memberFinder.findById(memberId)

        val schedule = StockMarketSchedule(
            title = request.title,
            content = request.content,
            scheduleDate = request.scheduleDate,
            member = member
        )

        val saved = stockMarketScheduleRepository.save(schedule)

        return StockMarketScheduleResponse(
            id = saved.id!!,
            title = saved.title,
            content = saved.content,
            scheduleDate = saved.scheduleDate,
            createdAt = saved.createdAt,
            updatedAt = saved.updatedAt
        )
    }
}
