package com.eltex.androidschool.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventType

@Entity(tableName = "Events")
data class EventEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "content")
    val content: String = "",
    @ColumnInfo(name = "author")
    val author: String = "",
    @ColumnInfo(name = "published")
    val published: String = "",
    @ColumnInfo(name = "type")
    val type: EventType = EventType.OFFLINE,
    @ColumnInfo(name = "datetime")
    val datetime: String = "",
    @ColumnInfo(name = "likedByMe")
    val likedByMe: Boolean = false,
    @ColumnInfo(name = "participatedByMe")
    val participatedByMe: Boolean = false,
    @ColumnInfo(name = "link")
    val link: String = "",
) {
    fun toEvent(): Event = Event(
        id = id,
        content = content,
        author = author,
        published = published,
//        type = type,
        datetime = datetime,
        likedByMe = likedByMe,
        participatedByMe = participatedByMe,
//        link = link
    )

    companion object {
        fun fromEvent(event: Event): EventEntity = with(event) {
            EventEntity(
                id = id,
                content = content,
                author = author,
                published = published,
//                type = type,
                datetime = datetime,
                likedByMe = likedByMe,
                participatedByMe = participatedByMe
            )
        }
    }
}