package yi.memberapi.application.impl.opinion.query

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.application.provided.OpinionRepository
import yi.memberapi.application.required.OpinionFinder
import yi.memberapi.domain.opinion.Opinion

@Service
@Transactional(readOnly = true)
class QueryOpinionFinder(
    private val opinionRepository: OpinionRepository
) : OpinionFinder {

    override fun findEntityById(id: Int): Opinion {
        return opinionRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Opinion not found: $id") }
    }
}
