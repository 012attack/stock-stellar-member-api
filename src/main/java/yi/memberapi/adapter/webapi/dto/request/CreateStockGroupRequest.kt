package yi.memberapi.adapter.webapi.dto.request

data class CreateStockGroupRequest(
    val title: String,
    val description: String? = null
)
