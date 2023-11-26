package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.EventType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class InMemoryEventRepository : EventRepository {
    private val state = MutableStateFlow(
        Event(
            id = 1L,
            content = "Приглашаю провести уютный вечер за увлекательными играми! У нас есть несколько вариантов настолок, подходящих для любой компании.",
            author = "Lydia Westervelt",
            published = "11.05.22 11:21",
            type = EventType.OFFLINE,
            datetime = "16.05.22 12:00",
            link = "https://m2.material.io/components/cards",
        )
    )

    override fun getEvent(): Flow<Event> = state.asStateFlow()
    override fun like() {
        state.update { it.copy(likedByMe = !it.likedByMe) }
    }

    override fun participate() {
        TODO("Not yet implemented")
    }

}