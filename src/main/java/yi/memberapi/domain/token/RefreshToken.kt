package yi.memberapi.domain.token

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity
class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val token: String,
    val username: String,
    val expiryDate: Instant,
    val createdAt: Instant = Instant.now()
) {
    protected constructor() : this(null, "", "", Instant.now(), Instant.now())

    fun isExpired(): Boolean = Instant.now().isAfter(expiryDate)
}
