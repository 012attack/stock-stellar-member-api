package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.newsgroup.dto.response.NewsGroupDetailResponse
import yi.memberapi.domain.newsgroup.NewsGroup

interface NewsGroupFinder {
    fun find(id: Int, memberId: Long): NewsGroupDetailResponse
    fun findEntityByIdWithMember(id: Int): NewsGroup
    fun findEntityByIdWithNews(id: Int): NewsGroup
}
