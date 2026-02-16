package yi.memberapi.application.impl.stockgroup.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.required.StockGroupFinder
import yi.memberapi.application.required.StockGroupStockRemover

@Service
@Transactional
class CommandStockGroupStockRemover(
    private val stockGroupFinder: StockGroupFinder
) : StockGroupStockRemover {

    override fun remove(groupId: Int, stockId: Int, memberId: Long) {
        val stockGroup = stockGroupFinder.findEntityByIdWithStocks(groupId)

        if (stockGroup.member?.id != memberId) {
            throw IllegalArgumentException("Not authorized to modify this stock group")
        }

        stockGroup.stocks.removeIf { it.id == stockId }
    }
}
