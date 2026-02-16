package yi.memberapi.application.impl.news.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.provided.NewsRepository
import yi.memberapi.application.provided.ThemeRepository
import yi.memberapi.application.required.NewsThemeAdder

@Service
@Transactional
class CommandNewsThemeAdder(
    private val newsRepository: NewsRepository,
    private val themeRepository: ThemeRepository
) : NewsThemeAdder {

    override fun add(newsId: Int, themeIds: List<Int>) {
        val news = newsRepository.findByIdWithPressAndThemes(newsId)
            ?: throw IllegalArgumentException("News not found: $newsId")

        val themes = themeRepository.findAllById(themeIds)
        news.themes.addAll(themes)
    }
}
