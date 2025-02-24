package com.eltex.androidschool.model

sealed interface PagingModel<out T> {
    data class Data<T>(val value: T) : PagingModel<T>
    data object Skeleton : PagingModel<Nothing>
    data class Error(val reason: Throwable) : PagingModel<Nothing>

}