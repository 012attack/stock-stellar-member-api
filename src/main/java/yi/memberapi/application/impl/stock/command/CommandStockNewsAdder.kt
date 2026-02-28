package yi.memberapi.application.impl.stock.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.required.news.NewsFinder
import yi.memberapi.application.required.stock.StockFinder
import yi.memberapi.application.required.stock.StockNewsAdder

@Service
@Transactional
class CommandStockNewsAdder(
    private val stockFinder: StockFinder,
    private val newsFinder: NewsFinder
) : StockNewsAdder {

    override fun add(stockId: Int, newsIds: List<Int>) {
        val stock = stockFinder.findEntityByIdWithNews(stockId)

        val newsList = newsFinder.findAllEntitiesByIds(newsIds)
        stock.news.addAll(newsList)
    }
}
