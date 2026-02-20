package yi.memberapi.adapter.webapi.newsgroup.dto.request

data class CreateNewsGroupRequest(
    val title: String,
    val description: String? = null
)
