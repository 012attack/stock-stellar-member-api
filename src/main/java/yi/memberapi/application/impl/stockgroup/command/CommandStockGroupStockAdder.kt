package yi.memberapi.application.impl.stockgroup.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.provided.StockGroupRepository
import yi.memberapi.application.provided.StockRepository
import yi.memberapi.application.required.StockGroupStockAdder

@Service
@Transactional
class CommandStockGroupStockAdder(
    private val stockGroupRepository: StockGroupRepository,
    private val stockRepository: StockRepository
) : StockGroupStockAdder {

    override fun add(groupId: Int, stockIds: List<Int>, memberId: Long) {
        val stockGroup = stockGroupRepository.findByIdWithStocks(groupId)
            ?: throw IllegalArgumentException("StockGroup not found: $groupId")

        if (stockGroup.member?.id != memberId) {
            throw IllegalArgumentException("Not authorized to modify this stock group")
        }

        val stocks = stockRepository.findAllById(stockIds)
        stockGroup.stocks.addAll(stocks)
    }
}
