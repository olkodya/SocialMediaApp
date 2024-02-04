package com.eltex.androidschool.adapter

import com.eltex.androidschool.model.Attachment

data class EventPayload(
    val liked: Boolean? = null,
    val participated: Boolean? = null,
    val likes: Int? = null,
    val participants: Int? = null,
    val content: String? = null,
    val attachment: Attachment? = null,
    val authorAvatar: String? = null
) {
    fun isNotEmpty(): Boolean =
        liked != null || participated != null || likes != null || participants != null || content != null || attachment != null || authorAvatar != null

}