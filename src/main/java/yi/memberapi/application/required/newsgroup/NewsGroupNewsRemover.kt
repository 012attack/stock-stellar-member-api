package yi.memberapi.application.required.newsgroup

interface NewsGroupNewsRemover {
    fun remove(groupId: Int, newsId: Int, memberId: Long)
}
