package yi.memberapi.adapter.webapi.dto.response

data class StockGroupListResponse(
    val stockGroups: List<StockGroupResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)
