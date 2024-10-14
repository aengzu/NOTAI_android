package com.iguana.notetaking.ai

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iguana.domain.model.ai.SummarizationStatus
import com.iguana.domain.usecase.RequestAISummarizationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AiViewModel @Inject constructor(
    private val requestAISummarizationUseCase: RequestAISummarizationUseCase
) : ViewModel() {

    private val _pageNumber = MutableLiveData<Int>()
    val pageNumber: LiveData<Int> get() = _pageNumber

    private val _aiRequestStatus = MutableLiveData<String>()
    val aiRequestStatus: LiveData<String> get() = _aiRequestStatus

    fun setPageNumber(pageNumber: Int) {
        _pageNumber.value = pageNumber
    }
    fun requestAISummarization(documentId: Long, pages: List<Int>) {
        viewModelScope.launch {
            val result = requestAISummarizationUseCase(documentId, pages)
            if (result.isSuccess) {
                _aiRequestStatus.value = SummarizationStatus.IN_PROGRESS.toString()
            } else {
                _aiRequestStatus.value = SummarizationStatus.FAILED.toString()
            }
        }
    }
}