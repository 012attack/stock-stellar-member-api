package yi.memberapi.application.impl.newsgroup.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.required.NewsFinder
import yi.memberapi.application.required.NewsGroupFinder
import yi.memberapi.application.required.NewsGroupNewsAdder

@Service
@Transactional
class CommandNewsGroupNewsAdder(
    private val newsGroupFinder: NewsGroupFinder,
    private val newsFinder: NewsFinder
) : NewsGroupNewsAdder {

    override fun add(groupId: Int, newsIds: List<Int>, memberId: Long) {
        val newsGroup = newsGroupFinder.findEntityByIdWithNews(groupId)

        if (newsGroup.member?.id != memberId) {
            throw IllegalArgumentException("Not authorized to modify this news group")
        }

        val newsList = newsFinder.findAllEntitiesByIds(newsIds)
        newsGroup.news.addAll(newsList)
    }
}
