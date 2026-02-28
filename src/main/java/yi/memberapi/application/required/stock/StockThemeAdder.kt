package yi.memberapi.application.required.stock

interface StockThemeAdder {
    fun add(stockId: Int, themeIds: List<Int>)
}
