package yi.memberapi.adapter.webapi.newsgroup.dto.request

data class UpdateNewsGroupRequest(
    val title: String,
    val description: String? = null
)
