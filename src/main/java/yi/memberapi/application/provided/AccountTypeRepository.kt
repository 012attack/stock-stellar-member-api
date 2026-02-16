package yi.memberapi.application.provided

import org.springframework.data.jpa.repository.JpaRepository
import yi.memberapi.domain.accounttype.AccountType

interface AccountTypeRepository : JpaRepository<AccountType, Int> {
    fun findByAccountNm(accountNm: String): AccountType?
}
