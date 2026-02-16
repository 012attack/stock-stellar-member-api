package yi.memberapi.application.impl.stock.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.required.StockFinder
import yi.memberapi.application.required.ThemeFinder
import yi.memberapi.application.required.StockThemeAdder

@Service
@Transactional
class CommandStockThemeAdder(
    private val stockFinder: StockFinder,
    private val themeFinder: ThemeFinder
) : StockThemeAdder {

    override fun add(stockId: Int, themeIds: List<Int>) {
        val stock = stockFinder.findEntityByIdWithThemes(stockId)

        val themes = themeFinder.findAllEntitiesByIds(themeIds)
        stock.themes.addAll(themes)
    }
}
