package yi.memberapi.domain.accounttype

import jakarta.persistence.*

@Entity
@Table(name = "account_types")
class AccountType(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @Column(name = "account_nm", nullable = false, length = 200, unique = true)
    val accountNm: String
) {
    protected constructor() : this(null, "")
}
