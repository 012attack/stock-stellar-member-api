package yi.memberapi.adapter.webapi

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginApi {

    @PostMapping("/login")
    fun login(): String {
        return "Login successful"
    }

}
