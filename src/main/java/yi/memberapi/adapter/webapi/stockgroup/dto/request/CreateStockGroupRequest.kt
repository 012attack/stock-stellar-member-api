package yi.memberapi.adapter.webapi.stockgroup.dto.request

data class CreateStockGroupRequest(
    val title: String,
    val description: String? = null
)
