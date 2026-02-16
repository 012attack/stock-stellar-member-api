package yi.memberapi.application.required

interface StockGroupStockAdder {
    fun add(groupId: Int, stockIds: List<Int>, memberId: Long)
}
