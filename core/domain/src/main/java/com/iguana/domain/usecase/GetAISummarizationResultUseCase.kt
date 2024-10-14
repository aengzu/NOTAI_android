package com.iguana.domain.usecase

import com.iguana.domain.model.ai.AIResult
import com.iguana.domain.repository.AIRepository
import javax.inject.Inject

class GetAISummarizationResultUseCase @Inject constructor(
    private val aiRepository: AIRepository
) {
    suspend operator fun invoke(documentId: Long): Result<List<AIResult>> {
        return aiRepository.getSummarization(documentId)
    }
}