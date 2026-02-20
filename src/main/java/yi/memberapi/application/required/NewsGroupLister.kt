package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.newsgroup.dto.response.NewsGroupListResponse

interface NewsGroupLister {
    fun list(page: Int, size: Int, memberId: Long, favoriteOnly: Boolean = false): NewsGroupListResponse
}
