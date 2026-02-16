package yi.memberapi.application.impl.stockgroup.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.required.StockFinder
import yi.memberapi.application.required.StockGroupFinder
import yi.memberapi.application.required.StockGroupStockAdder

@Service
@Transactional
class CommandStockGroupStockAdder(
    private val stockGroupFinder: StockGroupFinder,
    private val stockFinder: StockFinder
) : StockGroupStockAdder {

    override fun add(groupId: Int, stockIds: List<Int>, memberId: Long) {
        val stockGroup = stockGroupFinder.findEntityByIdWithStocks(groupId)

        if (stockGroup.member?.id != memberId) {
            throw IllegalArgumentException("Not authorized to modify this stock group")
        }

        val stocks = stockFinder.findAllEntitiesByIds(stockIds)
        stockGroup.stocks.addAll(stocks)
    }
}
