package yi.memberapi.application.impl.theme.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.news.dto.response.NewsResponse
import yi.memberapi.adapter.webapi.news.dto.response.PressResponse
import yi.memberapi.adapter.webapi.stock.dto.response.StockResponse
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeDetailResponse
import yi.memberapi.adapter.webapi.theme.dto.response.ThemeResponse
import yi.memberapi.application.provided.NewsRepository
import yi.memberapi.application.provided.StockRepository
import yi.memberapi.application.provided.ThemeRepository
import yi.memberapi.application.required.ThemeFinder
import yi.memberapi.domain.theme.Theme

@Service
@Transactional(readOnly = true)
class QueryThemeFinder(
    private val themeRepository: ThemeRepository,
    private val newsRepository: NewsRepository,
    private val stockRepository: StockRepository
) : ThemeFinder {

    override fun findAllEntitiesByIds(ids: List<Int>): List<Theme> {
        return themeRepository.findAllById(ids)
    }

    override fun findById(id: Int): ThemeDetailResponse? {
        val theme = themeRepository.findById(id).orElse(null) ?: return null

        val newsList = newsRepository.findByThemeId(id).map { news ->
            NewsResponse(
                id = news.id!!,
                title = news.title,
                link = news.link,
                press = news.press?.let { PressResponse(id = it.id!!, name = it.name) },
                themes = news.themes.map { t -> ThemeResponse(id = t.id!!, themeName = t.themeName) },
                createdAt = news.createdAt
            )
        }

        val stockList = stockRepository.findByThemeId(id).map { stock ->
            StockResponse(
                id = stock.id!!,
                stockCode = stock.stockCode,
                stockName = stock.stockName,
                companySummary = stock.companySummary,
                themes = stock.themes.map { t -> ThemeResponse(id = t.id!!, themeName = t.themeName) }
            )
        }

        return ThemeDetailResponse(
            id = theme.id!!,
            themeName = theme.themeName,
            news = newsList,
            stocks = stockList
        )
    }
}
