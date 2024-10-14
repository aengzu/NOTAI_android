package com.iguana.notetaking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotetakingViewModel : ViewModel() {
    private val _isSideBarVisible = MutableLiveData(true) // 초기값은 true로 설정
    val isSideBarVisible: LiveData<Boolean> get() = _isSideBarVisible

    private var _pdfId: Long = 0
    val pdfId: Long get() = _pdfId

    private val _pdfUri = MutableLiveData<String?>()
    val pdfUri: MutableLiveData<String?> get() = _pdfUri
    private val _pdfTitle = MutableLiveData<String>()
    val pdfTitle: LiveData<String> get() = _pdfTitle


    // PDF ID를 설정하는 함수
    fun setPdf(id: Long, uri: String?, title: String) {
        _pdfId = id
        _pdfUri.value = uri
        _pdfTitle.value = title
    }

    // 사이드바의 가시성 상태를 토글하는 함수
    fun toggleSideBarVisibility() {
        _isSideBarVisible.value = _isSideBarVisible.value?.not()
    }
}
