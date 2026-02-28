package yi.memberapi.application.required.stock

interface StockNewsAdder {
    fun add(stockId: Int, newsIds: List<Int>)
}
