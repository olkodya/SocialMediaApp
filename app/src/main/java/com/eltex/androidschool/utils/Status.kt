package com.eltex.androidschool.utils


interface Status {
    data object Idle : Status
    data object Loading : Status
    data class Error(val reason: Throwable) : Status
}