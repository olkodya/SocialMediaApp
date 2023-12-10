package com.eltex.androidschool.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("content")
    val content: String = "",
    @SerialName("author")
    val author: String = "",
    @SerialName("published")
    val published: String = "",
    @SerialName("type")
    val type: EventType = EventType.OFFLINE,
    @SerialName("datetime")
    val datetime: String = "",
    @SerialName("likedByMe")
    val likedByMe: Boolean = false,
    @SerialName("participatedByMe")
    val participatedByMe: Boolean = false,
    @SerialName("link")
    val link: String = "",
)

