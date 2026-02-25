package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.news.dto.response.NewsListResponse
import yi.memberapi.domain.readcheck.ReadFilter
import java.math.BigDecimal

interface NewsLister {
    fun list(page: Int, size: Int, title: String?, pressName: String?, themeName: String?, favoriteOnly: Boolean = false, memberId: Long? = null, minScore: BigDecimal? = null, readFilter: ReadFilter? = null): NewsListResponse
}
