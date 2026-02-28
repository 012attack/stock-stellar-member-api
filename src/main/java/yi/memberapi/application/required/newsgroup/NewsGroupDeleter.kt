package yi.memberapi.application.required.newsgroup

interface NewsGroupDeleter {
    fun delete(id: Int, memberId: Long)
}
