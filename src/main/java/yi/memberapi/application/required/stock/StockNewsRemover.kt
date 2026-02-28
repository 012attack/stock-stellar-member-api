package yi.memberapi.application.required.stock

interface StockNewsRemover {
    fun remove(stockId: Int, newsId: Int)
}
