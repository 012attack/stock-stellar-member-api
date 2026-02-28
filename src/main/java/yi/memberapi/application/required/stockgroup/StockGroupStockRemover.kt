package yi.memberapi.application.required.stockgroup

interface StockGroupStockRemover {
    fun remove(groupId: Int, stockId: Int, memberId: Long)
}
