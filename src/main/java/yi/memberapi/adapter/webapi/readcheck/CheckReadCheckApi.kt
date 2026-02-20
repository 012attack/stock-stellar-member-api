package yi.memberapi.adapter.webapi.readcheck

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.readcheck.dto.response.ReadCheckCheckResponse
import yi.memberapi.application.required.ReadCheckChecker
import yi.memberapi.domain.readcheck.ReadCheckTargetType

@RestController
@RequestMapping("/api/read-checks")
class CheckReadCheckApi(
    private val readCheckChecker: ReadCheckChecker
) {

    @GetMapping("/check")
    fun checkReadCheck(
        @RequestParam targetType: ReadCheckTargetType,
        @RequestParam targetId: Int
    ): ResponseEntity<ReadCheckCheckResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        val response = readCheckChecker.check(targetType, targetId, memberId)
        return ResponseEntity.ok(response)
    }
}
