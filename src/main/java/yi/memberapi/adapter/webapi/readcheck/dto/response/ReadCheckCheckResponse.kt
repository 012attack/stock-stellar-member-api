package yi.memberapi.adapter.webapi.readcheck.dto.response

data class ReadCheckCheckResponse(
    val targetType: String,
    val targetId: Int,
    val checked: Boolean
)
