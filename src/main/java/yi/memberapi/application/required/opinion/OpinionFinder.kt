package yi.memberapi.application.required.opinion

import yi.memberapi.domain.opinion.Opinion

interface OpinionFinder {
    fun findEntityById(id: Int): Opinion
}
