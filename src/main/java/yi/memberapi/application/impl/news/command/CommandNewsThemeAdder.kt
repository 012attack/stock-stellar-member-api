package yi.memberapi.application.impl.news.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.required.NewsFinder
import yi.memberapi.application.required.ThemeFinder
import yi.memberapi.application.required.NewsThemeAdder

@Service
@Transactional
class CommandNewsThemeAdder(
    private val newsFinder: NewsFinder,
    private val themeFinder: ThemeFinder
) : NewsThemeAdder {

    override fun add(newsId: Int, themeIds: List<Int>) {
        val news = newsFinder.findEntityByIdWithPressAndThemes(newsId)

        val themes = themeFinder.findAllEntitiesByIds(themeIds)
        news.themes.addAll(themes)
    }
}
