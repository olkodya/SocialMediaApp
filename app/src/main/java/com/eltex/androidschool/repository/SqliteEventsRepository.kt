package com.eltex.androidschool.repository

import com.eltex.androidschool.dao.EventDao
import com.eltex.androidschool.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SqliteEventsRepository(private val dao: EventDao) : EventRepository {
    private val state = MutableStateFlow(readEvents())

    private fun readEvents(): List<Event> = dao.getAll()
    override fun getEvents() = state.asStateFlow()
    override fun likeById(id: Long) {
        dao.likeById(id)
        state.update { readEvents() }
    }

    override fun participateById(id: Long) {
        dao.participateById(id)
        state.update { readEvents() }
    }

    override fun addEvent(content: String) {
        dao.save(Event(content = content, author = "Me"))
        state.update { readEvents() }

    }

    override fun deleteById(id: Long) {
        dao.deleteById(id)
        state.update { readEvents() }
    }

    override fun editById(id: Long, content: String) {
        dao.editById(id, content)
        state.update { readEvents() }
    }
}