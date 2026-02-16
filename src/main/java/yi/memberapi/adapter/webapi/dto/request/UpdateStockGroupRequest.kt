package yi.memberapi.adapter.webapi.dto.request

data class UpdateStockGroupRequest(
    val title: String,
    val description: String? = null
)
