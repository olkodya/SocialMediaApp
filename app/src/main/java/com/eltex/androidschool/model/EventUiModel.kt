package com.eltex.androidschool.model

data class EventUiModel(
    val id: Long = 0L,
    val content: String = "",
    val datetime: String = "",
    val author: String = "",
    val published: String = "",
//    @SerialName("type")
//    val type: EventType = EventType.OFFLINE,
    val likedByMe: Boolean = false,
    val participatedByMe: Boolean = false,
    val likes: Int = 0,
    val participants: Int = 0,

//    @SerialName("link")
//    val link: String = "",
)

