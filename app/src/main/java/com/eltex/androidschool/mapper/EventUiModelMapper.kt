package com.eltex.androidschool.mapper

import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventUiModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class EventUiModelMapper {

    private companion object {
        val FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm")
    }

    fun map(event: Event): EventUiModel = with(event) {
        EventUiModel(
            id = id,
            content = content,
            datetime = datetime,
            author = author,
            published = FORMATTER.format(published.atZone(ZoneId.systemDefault())),
            likedByMe = likedByMe,
            participatedByMe = participatedByMe,
            likes = likeOwnerIds.size,
            participants = participantsIds.size
        )
    }
}