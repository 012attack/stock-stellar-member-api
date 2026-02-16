package yi.memberapi.application.required

interface StockGroupDeleter {
    fun delete(id: Int, memberId: Long)
}
