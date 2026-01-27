package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.dto.request.RegisterRequest
import yi.memberapi.adapter.webapi.dto.response.RegisterResponse

interface MemberRegister {
    fun register(request: RegisterRequest): RegisterResponse
}
