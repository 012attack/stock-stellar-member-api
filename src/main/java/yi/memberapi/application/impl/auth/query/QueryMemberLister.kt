package yi.memberapi.application.impl.auth.query

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.response.MemberListResponse
import yi.memberapi.adapter.webapi.dto.response.MemberResponse
import yi.memberapi.application.provided.MemberRepository
import yi.memberapi.application.required.MemberLister

@Service
@Transactional(readOnly = true)
class QueryMemberLister(
    private val memberRepository: MemberRepository
) : MemberLister {

    override fun list(page: Int, size: Int): MemberListResponse {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"))
        val memberPage = memberRepository.findAll(pageable)

        val members = memberPage.content.map { member ->
            MemberResponse(
                id = member.id!!,
                username = member.username!!,
                name = member.name!!
            )
        }

        return MemberListResponse(
            members = members,
            page = memberPage.number,
            size = memberPage.size,
            totalElements = memberPage.totalElements,
            totalPages = memberPage.totalPages
        )
    }
}
