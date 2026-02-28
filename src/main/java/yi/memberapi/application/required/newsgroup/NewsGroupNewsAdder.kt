package yi.memberapi.application.required.newsgroup

interface NewsGroupNewsAdder {
    fun add(groupId: Int, newsIds: List<Int>, memberId: Long)
}
