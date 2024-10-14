package com.iguana.domain.usecase

import com.iguana.domain.model.ai.AIStatusResult
import com.iguana.domain.repository.AIRepository
import javax.inject.Inject

class CheckAIStatusUseCase @Inject constructor(
    private val aiRepository: AIRepository
) {
    suspend operator fun invoke(documentId: Long): Result<AIStatusResult> {
        return aiRepository.checkStatus(documentId)
    }
}
