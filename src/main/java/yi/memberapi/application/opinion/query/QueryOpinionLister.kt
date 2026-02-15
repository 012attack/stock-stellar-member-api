package yi.memberapi.application.opinion.query

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yi.memberapi.adapter.webapi.dto.response.*
import yi.memberapi.application.provided.DailyTop30RecordRepository
import yi.memberapi.application.provided.NewsRepository
import yi.memberapi.application.provided.OpinionRepository
import yi.memberapi.application.required.OpinionLister
import yi.memberapi.domain.opinion.Opinion
import yi.memberapi.domain.opinion.TargetType

@Service
@Transactional(readOnly = true)
class QueryOpinionLister(
    private val opinionRepository: OpinionRepository,
    private val newsRepository: NewsRepository,
    private val dailyTop30RecordRepository: DailyTop30RecordRepository
) : OpinionLister {

    override fun listByTarget(targetType: TargetType, targetId: Int, page: Int, size: Int): OpinionListResponse {
        val pageable = PageRequest.of(page, size)
        val opinionPage = opinionRepository.findByTargetTypeAndTargetId(targetType, targetId, pageable)
        return toResponse(opinionPage)
    }

    override fun listByTargetType(targetType: TargetType, page: Int, size: Int): OpinionListResponse {
        val pageable = PageRequest.of(page, size)
        val opinionPage = opinionRepository.findByTargetType(targetType, pageable)
        return toResponse(opinionPage)
    }

    private fun toResponse(opinionPage: Page<Opinion>): OpinionListResponse {
        val opinions = opinionPage.content
        val targets = resolveTargets(opinions)

        val opinionResponses = opinions.map { opinion ->
            OpinionResponse(
                id = opinion.id!!,
                title = opinion.title,
                content = opinion.content,
                memberName = opinion.member?.name ?: "",
                createdAt = opinion.createdAt,
                targetType = opinion.targetType.name,
                targetId = opinion.targetId,
                target = targets["${opinion.targetType}_${opinion.targetId}"]
            )
        }

        return OpinionListResponse(
            opinions = opinionResponses,
            page = opinionPage.number,
            size = opinionPage.size,
            totalElements = opinionPage.totalElements,
            totalPages = opinionPage.totalPages
        )
    }

    private fun resolveTargets(opinions: List<Opinion>): Map<String, OpinionTargetResponse> {
        val targets = mutableMapOf<String, OpinionTargetResponse>()

        val newsIds = opinions.filter { it.targetType == TargetType.NEWS }.map { it.targetId }.distinct()
        val recordIds = opinions.filter { it.targetType == TargetType.RECORD }.map { it.targetId }.distinct()

        if (newsIds.isNotEmpty()) {
            newsRepository.findAllById(newsIds).forEach { news ->
                targets["NEWS_${news.id}"] = OpinionTargetResponse(
                    targetType = "NEWS",
                    news = OpinionTargetResponse.OpinionNewsTarget(
                        id = news.id!!,
                        title = news.title,
                        link = news.link,
                        press = news.press?.let { PressResponse(it.id!!, it.name) },
                        createdAt = news.createdAt
                    ),
                    record = null
                )
            }
        }

        if (recordIds.isNotEmpty()) {
            dailyTop30RecordRepository.findAllById(recordIds).forEach { record ->
                targets["RECORD_${record.id}"] = OpinionTargetResponse(
                    targetType = "RECORD",
                    news = null,
                    record = OpinionTargetResponse.OpinionRecordTarget(
                        id = record.id!!,
                        recordDate = record.recordDate,
                        rank = record.rank,
                        changeRate = record.changeRate,
                        stock = record.stock?.let { StockResponse(it.id!!, it.stockCode, it.stockName, it.companySummary) },
                        themes = record.themes.map { ThemeResponse(it.id!!, it.themeName) }
                    )
                )
            }
        }

        return targets
    }
}
