package com.iguana.notetaking.ai

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.iguana.domain.usecase.CheckAIStatusUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CheckAIStatusWorker @Inject constructor(
    @ApplicationContext context: Context,
    workerParams: WorkerParameters,
    private val checkAIStatusUseCase: CheckAIStatusUseCase
): Worker(context, workerParams){
    override fun doWork(): Result {
        val documentId = inputData.getLong("documentId", -1)

        if (documentId == -1L) return Result.failure()

        CoroutineScope(Dispatchers.IO).launch {
            val result = checkAIStatusUseCase(documentId)
            result.onSuccess { statusResult ->
                // 상태 업데이트 로직 추가
                if (statusResult.isCompleted()) {
                    // 요약 완료 시 결과 확인
                    Log.d("testt", "요약 완료")
                }
            }
            result.onFailure {
                // 실패 처리 로직 추가
                Log.d("testt", "요약 실패")
            }
        }
        return Result.success()
    }
}