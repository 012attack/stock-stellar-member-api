package yi.memberapi.application.news.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.provided.NewsRepository
import yi.memberapi.application.required.NewsThemeRemover

@Service
@Transactional
class CommandNewsThemeRemover(
    private val newsRepository: NewsRepository
) : NewsThemeRemover {

    override fun remove(newsId: Int, themeId: Int) {
        val news = newsRepository.findByIdWithPressAndThemes(newsId)
            ?: throw IllegalArgumentException("News not found: $newsId")

        news.themes.removeIf { it.id == themeId }
    }
}
