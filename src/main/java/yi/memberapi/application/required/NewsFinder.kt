package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.response.NewsResponse

interface NewsFinder {
    fun findById(id: Int): NewsResponse?
}
