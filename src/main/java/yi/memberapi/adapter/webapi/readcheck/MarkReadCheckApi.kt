package yi.memberapi.adapter.webapi.readcheck

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.readcheck.dto.request.MarkReadCheckRequest
import yi.memberapi.adapter.webapi.readcheck.dto.response.ReadCheckResponse
import yi.memberapi.application.required.ReadCheckMarker
import java.net.URI

@RestController
@RequestMapping("/api/read-checks")
class MarkReadCheckApi(
    private val readCheckMarker: ReadCheckMarker
) {

    @PostMapping
    fun markReadCheck(@RequestBody request: MarkReadCheckRequest): ResponseEntity<ReadCheckResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!

        val response = readCheckMarker.mark(request, memberId)
        return ResponseEntity.created(URI.create("/api/read-checks/${response.id}")).body(response)
    }
}
