package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.response.NewsListResponse

interface NewsLister {
    fun list(page: Int, size: Int, title: String?, pressName: String?): NewsListResponse
}
