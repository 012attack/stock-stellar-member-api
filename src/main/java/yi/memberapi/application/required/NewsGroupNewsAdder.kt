package yi.memberapi.application.required

interface NewsGroupNewsAdder {
    fun add(groupId: Int, newsIds: List<Int>, memberId: Long)
}
