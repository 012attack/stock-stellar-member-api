package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.response.NewsResponse
import yi.memberapi.domain.news.News

interface NewsFinder {
    fun findById(id: Int): NewsResponse?
    fun findEntityByIdWithPressAndThemes(id: Int): News
}
