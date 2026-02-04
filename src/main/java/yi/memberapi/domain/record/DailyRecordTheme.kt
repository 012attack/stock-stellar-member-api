package yi.memberapi.domain.record

import jakarta.persistence.*
import yi.memberapi.domain.theme.Theme

@Entity
@Table(name = "daily_record_themes")
class DailyRecordTheme(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "daily_record_id")
    val dailyRecord: DailyTop30Record? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    val theme: Theme? = null
) {
    protected constructor() : this(null, null, null)
}
