package com.eltex.androidschool.model

data class EventUiModel(
    val id: Long = 0L,
    val content: String = "",
    val datetime: String = "",
    val author: String = "",
    val published: String = "",
    val likedByMe: Boolean = false,
    val participatedByMe: Boolean = false,
    val likes: Int = 0,
    val participants: Int = 0,
    val attachment: Attachment? = null,
    val authorAvatar: String? = null
)

