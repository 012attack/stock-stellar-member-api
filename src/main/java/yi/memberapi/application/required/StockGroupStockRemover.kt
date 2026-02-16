package yi.memberapi.application.required

interface StockGroupStockRemover {
    fun remove(groupId: Int, stockId: Int, memberId: Long)
}
