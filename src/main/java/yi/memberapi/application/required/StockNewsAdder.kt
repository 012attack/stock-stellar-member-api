package yi.memberapi.application.required

interface StockNewsAdder {
    fun add(stockId: Int, newsIds: List<Int>)
}
