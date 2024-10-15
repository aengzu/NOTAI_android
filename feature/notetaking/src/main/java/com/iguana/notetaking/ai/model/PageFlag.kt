package com.iguana.notetaking.ai.model


// 각 페이지의 상태를 나타내는 enum 클래스
enum class PageStatus {
    NOT_STARTED,
    IN_PROGRESS,
    FINISHED
}

// 각 페이지의 상태를 관리하는 데이터 클래스
data class PageFlag(
    val pageNumber: Int,      // 페이지 번호
    val status: PageStatus    // 페이지 상태
)
