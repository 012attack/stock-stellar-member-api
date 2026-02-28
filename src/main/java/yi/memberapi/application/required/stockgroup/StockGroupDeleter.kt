package yi.memberapi.application.required.stockgroup

interface StockGroupDeleter {
    fun delete(id: Int, memberId: Long)
}
