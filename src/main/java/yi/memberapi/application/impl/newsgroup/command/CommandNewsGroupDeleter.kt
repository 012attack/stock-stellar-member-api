package yi.memberapi.application.impl.newsgroup.command

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.provided.NewsGroupRepository
import yi.memberapi.application.required.NewsGroupDeleter
import yi.memberapi.application.required.NewsGroupFinder

@Service
@Transactional
class CommandNewsGroupDeleter(
    private val newsGroupRepository: NewsGroupRepository,
    private val newsGroupFinder: NewsGroupFinder
) : NewsGroupDeleter {

    override fun delete(id: Int, memberId: Long) {
        val newsGroup = newsGroupFinder.findEntityByIdWithMember(id)

        if (newsGroup.member?.id != memberId) {
            throw IllegalArgumentException("Not authorized to delete this news group")
        }

        newsGroupRepository.delete(newsGroup)
    }
}
