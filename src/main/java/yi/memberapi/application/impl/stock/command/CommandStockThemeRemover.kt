package yi.memberapi.application.impl.stock.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.required.stock.StockFinder
import yi.memberapi.application.required.stock.StockThemeRemover

@Service
@Transactional
class CommandStockThemeRemover(
    private val stockFinder: StockFinder
) : StockThemeRemover {

    override fun remove(stockId: Int, themeId: Int) {
        val stock = stockFinder.findEntityByIdWithThemes(stockId)

        stock.themes.removeIf { it.id == themeId }
    }
}
