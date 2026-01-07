package yi.memberapi.domain.member

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val username: String? = null,

    val password: String? = null,

    val name: String? = null
) {
    protected constructor() : this(null, null, null, null)
}
