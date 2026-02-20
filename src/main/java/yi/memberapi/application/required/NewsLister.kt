package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.news.dto.response.NewsListResponse

interface NewsLister {
    fun list(page: Int, size: Int, title: String?, pressName: String?, themeName: String?, favoriteOnly: Boolean = false, memberId: Long? = null): NewsListResponse
}
