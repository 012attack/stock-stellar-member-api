package yi.memberapi.adapter.webapi.auth

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.webapi.auth.dto.response.MemberListResponse
import yi.memberapi.application.required.MemberLister

@RestController
@RequestMapping("/api/members")
class GetMemberListApi(
    private val memberLister: MemberLister
) {

    @GetMapping
    fun getMembers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int
    ): ResponseEntity<MemberListResponse> {
        val response = memberLister.list(page, size)
        return ResponseEntity.ok(response)
    }
}
