package com.iguana.domain.usecase

import com.iguana.domain.repository.AIRepository
import javax.inject.Inject

class RequestAISummarizationUseCase @Inject constructor(
    private val aiRepository: AIRepository
) {
    suspend operator fun invoke(documentId:  Long, pages: List<Int>): Result<Unit> {
        return aiRepository.requestSummarization(documentId, pages)
    }
}