package yi.memberapi.application.impl.newsgroup.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.required.NewsGroupFinder
import yi.memberapi.application.required.NewsGroupNewsRemover

@Service
@Transactional
class CommandNewsGroupNewsRemover(
    private val newsGroupFinder: NewsGroupFinder
) : NewsGroupNewsRemover {

    override fun remove(groupId: Int, newsId: Int, memberId: Long) {
        val newsGroup = newsGroupFinder.findEntityByIdWithNews(groupId)

        if (newsGroup.member?.id != memberId) {
            throw IllegalArgumentException("Not authorized to modify this news group")
        }

        newsGroup.news.removeIf { it.id == newsId }
    }
}
