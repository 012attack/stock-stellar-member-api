package yi.memberapi.application.required

interface NewsGroupNewsRemover {
    fun remove(groupId: Int, newsId: Int, memberId: Long)
}
