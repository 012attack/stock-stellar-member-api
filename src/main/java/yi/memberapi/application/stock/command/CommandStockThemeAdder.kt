package yi.memberapi.application.stock.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.provided.StockRepository
import yi.memberapi.application.provided.ThemeRepository
import yi.memberapi.application.required.StockThemeAdder

@Service
@Transactional
class CommandStockThemeAdder(
    private val stockRepository: StockRepository,
    private val themeRepository: ThemeRepository
) : StockThemeAdder {

    override fun add(stockId: Int, themeIds: List<Int>) {
        val stock = stockRepository.findByIdWithThemes(stockId)
            ?: throw IllegalArgumentException("Stock not found: $stockId")

        val themes = themeRepository.findAllById(themeIds)
        stock.themes.addAll(themes)
    }
}
