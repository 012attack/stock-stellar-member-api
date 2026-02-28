package yi.memberapi.application.required.stockgroup

interface StockGroupStockAdder {
    fun add(groupId: Int, stockIds: List<Int>, memberId: Long)
}
