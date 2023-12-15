package com.eltex.androidschool.repository

import com.eltex.androidschool.dao.EventDao
import com.eltex.androidschool.entity.EventEntity
import com.eltex.androidschool.model.Event
import kotlinx.coroutines.flow.map

class RoomEventsRepository(private val dao: EventDao) : EventRepository {

    override fun getEvents() = dao.getAll()
        .map {
            it.map(EventEntity::toEvent)
        }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun participateById(id: Long) {
        dao.participateById(id)
    }

    override fun addEvent(content: String) {
        dao.save(
            EventEntity.fromEvent(
                Event(content = content, author = "Me")
            )
        )

    }

    override fun deleteById(id: Long) {
        dao.deleteById(id)
    }

    override fun editById(id: Long, content: String) {
        dao.editById(id, content)
    }
}