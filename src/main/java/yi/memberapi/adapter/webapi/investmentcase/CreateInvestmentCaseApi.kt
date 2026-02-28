package yi.memberapi.adapter.webapi.investmentcase

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import yi.memberapi.adapter.security.MemberUserDetails
import yi.memberapi.adapter.webapi.investmentcase.dto.request.CreateInvestmentCaseRequest
import yi.memberapi.adapter.webapi.investmentcase.dto.response.InvestmentCaseResponse
import yi.memberapi.application.required.investmentcase.InvestmentCaseCreator
import java.net.URI

@RestController
@RequestMapping("/api/investment-cases")
class CreateInvestmentCaseApi(
    private val investmentCaseCreator: InvestmentCaseCreator
) {

    @PostMapping
    fun create(@RequestBody request: CreateInvestmentCaseRequest): ResponseEntity<InvestmentCaseResponse> {
        val memberUserDetails = SecurityContextHolder.getContext().authentication!!.principal as MemberUserDetails
        val memberId = memberUserDetails.getMember().id!!
        val response = investmentCaseCreator.create(request, memberId)
        return ResponseEntity.created(URI.create("/api/investment-cases/${response.id}")).body(response)
    }
}
