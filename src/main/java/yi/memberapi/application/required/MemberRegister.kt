package yi.memberapi.application.required

import yi.memberapi.adapter.webapi.auth.dto.request.RegisterRequest
import yi.memberapi.adapter.webapi.auth.dto.response.RegisterResponse

interface MemberRegister {
    fun register(request: RegisterRequest): RegisterResponse
}
