package yi.memberapi.application.impl.schedule.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.provided.StockMarketScheduleRepository
import yi.memberapi.application.required.StockMarketScheduleDeleter
import yi.memberapi.application.required.StockMarketScheduleFinder

@Service
@Transactional
class CommandStockMarketScheduleDeleter(
    private val stockMarketScheduleRepository: StockMarketScheduleRepository,
    private val stockMarketScheduleFinder: StockMarketScheduleFinder
) : StockMarketScheduleDeleter {

    override fun delete(id: Int, memberId: Long) {
        val schedule = stockMarketScheduleFinder.findEntityByIdWithMember(id)

        if (schedule.member?.id != memberId) {
            throw IllegalArgumentException("Not authorized to delete this schedule")
        }

        stockMarketScheduleRepository.delete(schedule)
    }
}
