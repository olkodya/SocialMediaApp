package com.eltex.androidschool.model

import com.eltex.androidschool.utils.InstantSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Event(
    @SerialName("id")
    val id: Long = 0L,
    @SerialName("content")
    val content: String = "",
    @SerialName("datetime")
    val datetime: String = "",
    @SerialName("author")
    val author: String = "",
    @SerialName("published")
    @Serializable(InstantSerializer::class)
    val published: Instant = Instant.now(),
//    @SerialName("type")
//    val type: EventType = EventType.OFFLINE,
    @SerialName("likedByMe")
    val likedByMe: Boolean = false,
    @SerialName("participatedByMe")
    val participatedByMe: Boolean = false,
    @SerialName("likeOwnerIds")
    val likeOwnerIds: Set<Long> = emptySet(),
    @SerialName("participantsIds")
    val participantsIds: Set<Long> = emptySet(),
//    @SerialName("link")
//    val link: String = "",
)

