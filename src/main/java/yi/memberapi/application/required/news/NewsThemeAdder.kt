package yi.memberapi.application.required.news

interface NewsThemeAdder {
    fun add(newsId: Int, themeIds: List<Int>)
}
