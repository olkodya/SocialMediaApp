package com.eltex.androidschool.adapter

data class EventPayload(
    val liked: Boolean? = null,
    val participated: Boolean? = null,
) {
    fun isNotEmpty(): Boolean = (liked != null || participated != null)

}