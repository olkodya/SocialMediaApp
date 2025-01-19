package com.eltex.androidschool.model

sealed interface EventStatus {
    data class Idle(val finish: Boolean = false) : EventStatus
    data object NextPageLoading : EventStatus
    data object InitialLoading : EventStatus
    data object Refreshing : EventStatus
    data class EmptyError(val reason: Throwable) : EventStatus
    data class NextPageError(val reason: Throwable) : EventStatus

}
