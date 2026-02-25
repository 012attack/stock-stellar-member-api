package yi.memberapi.application.impl.stock.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.required.StockFinder
import yi.memberapi.application.required.StockNewsRemover

@Service
@Transactional
class CommandStockNewsRemover(
    private val stockFinder: StockFinder
) : StockNewsRemover {

    override fun remove(stockId: Int, newsId: Int) {
        val stock = stockFinder.findEntityByIdWithNews(stockId)

        stock.news.removeIf { it.id == newsId }
    }
}
