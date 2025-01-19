package com.eltex.androidschool.adapter

data class EventPayload(
    val liked: Boolean? = null,
    val participated: Boolean? = null,
    val likes: Int? = null,
    val participants: Int? = null,
) {
    fun isNotEmpty(): Boolean =
        (liked != null || participated != null || likes != null || participants != null)
}