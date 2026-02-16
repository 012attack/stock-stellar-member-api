package yi.memberapi.application.impl.news.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.required.NewsFinder
import yi.memberapi.application.required.NewsThemeRemover

@Service
@Transactional
class CommandNewsThemeRemover(
    private val newsFinder: NewsFinder
) : NewsThemeRemover {

    override fun remove(newsId: Int, themeId: Int) {
        val news = newsFinder.findEntityByIdWithPressAndThemes(newsId)

        news.themes.removeIf { it.id == themeId }
    }
}
