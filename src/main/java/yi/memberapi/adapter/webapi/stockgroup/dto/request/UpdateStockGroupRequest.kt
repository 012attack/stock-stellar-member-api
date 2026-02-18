package yi.memberapi.adapter.webapi.stockgroup.dto.request

data class UpdateStockGroupRequest(
    val title: String,
    val description: String? = null
)
