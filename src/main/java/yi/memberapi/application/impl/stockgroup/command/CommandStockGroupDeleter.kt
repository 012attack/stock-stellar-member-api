package yi.memberapi.application.impl.stockgroup.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.provided.StockGroupRepository
import yi.memberapi.application.required.StockGroupDeleter
import yi.memberapi.application.required.StockGroupFinder

@Service
@Transactional
class CommandStockGroupDeleter(
    private val stockGroupRepository: StockGroupRepository,
    private val stockGroupFinder: StockGroupFinder
) : StockGroupDeleter {

    override fun delete(id: Int, memberId: Long) {
        val stockGroup = stockGroupFinder.findEntityByIdWithMember(id)

        if (stockGroup.member?.id != memberId) {
            throw IllegalArgumentException("Not authorized to delete this stock group")
        }

        stockGroupRepository.delete(stockGroup)
    }
}
