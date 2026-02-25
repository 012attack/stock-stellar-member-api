package yi.memberapi.adapter.webapi.investmentcase

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.investmentcase.dto.request.UpdateInvestmentCaseRequest
import yi.memberapi.adapter.webapi.investmentcase.dto.response.InvestmentCaseResponse
import yi.memberapi.application.required.InvestmentCaseUpdater

@RestController
@RequestMapping("/api/investment-cases")
class UpdateInvestmentCaseApi(
    private val investmentCaseUpdater: InvestmentCaseUpdater
) {

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Int,
        @RequestBody request: UpdateInvestmentCaseRequest
    ): ResponseEntity<InvestmentCaseResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!
        val response = investmentCaseUpdater.update(id, request, memberId)
        return ResponseEntity.ok(response)
    }
}
