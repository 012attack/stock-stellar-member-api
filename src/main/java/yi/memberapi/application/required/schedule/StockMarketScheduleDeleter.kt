package yi.memberapi.application.required.schedule

interface StockMarketScheduleDeleter {
    fun delete(id: Int, memberId: Long)
}
