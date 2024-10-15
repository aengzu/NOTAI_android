package com.iguana.notetaking.ai

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.iguana.domain.model.ai.AIStatusResult
import com.iguana.domain.model.ai.SummarizationStatus
import com.iguana.domain.usecase.CheckAIStatusUseCase
import com.iguana.domain.usecase.GetAISummarizationResultUseCase
import com.iguana.domain.usecase.RequestAISummarizationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AiViewModel @Inject constructor(
    private val requestAISummarizationUseCase: RequestAISummarizationUseCase,
    private val checkAIStatusUseCase: CheckAIStatusUseCase,
    private val getAISummarizationResultUseCase: GetAISummarizationResultUseCase
    ) : ViewModel() {

    private val _pageNumber = MutableLiveData<Int>()
    val pageNumber: LiveData<Int> get() = _pageNumber

    private val _aiRequestStatus = MutableLiveData<String>()
    val aiRequestStatus: LiveData<String> get() = _aiRequestStatus

    private val _aiProgress = MutableLiveData<Int>()
    val aiProgress: LiveData<Int> get() = _aiProgress


    fun setPageNumber(pageNumber: Int) {
        _pageNumber.value = pageNumber
    }
    fun requestAISummarization(documentId: Long, pages: List<Int>) {
        viewModelScope.launch {
            val result = requestAISummarizationUseCase(documentId, pages)
            if (result.isSuccess) {
                startAIStatusCheck(documentId)
                _aiRequestStatus.value = SummarizationStatus.IN_PROGRESS.toString()
            } else {
                _aiRequestStatus.value = SummarizationStatus.FAILED.toString()
            }
        }
    }

    fun updateProgress(statusResult: AIStatusResult) {
        _aiProgress.value = statusResult.getProgressPercentage()
        _aiRequestStatus.value = statusResult.getStatusMessage()
    }

    private fun startAIStatusCheck(documentId: Long) {
        // WorkManager를 사용하여 백그라운드 작업을 설정
        val data = workDataOf("documentId" to documentId)

        val checkAIStatusRequest = PeriodicWorkRequestBuilder<CheckAIStatusWorker>(
            1, TimeUnit.MINUTES // 1분 주기로 작업 실행
        )
            .setInputData(data)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            "checkAIStatusWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            checkAIStatusRequest
        )
    }
}