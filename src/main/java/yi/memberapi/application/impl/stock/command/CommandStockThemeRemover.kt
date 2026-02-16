package yi.memberapi.application.impl.stock.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.provided.StockRepository
import yi.memberapi.application.required.StockThemeRemover

@Service
@Transactional
class CommandStockThemeRemover(
    private val stockRepository: StockRepository
) : StockThemeRemover {

    override fun remove(stockId: Int, themeId: Int) {
        val stock = stockRepository.findByIdWithThemes(stockId)
            ?: throw IllegalArgumentException("Stock not found: $stockId")

        stock.themes.removeIf { it.id == themeId }
    }
}
