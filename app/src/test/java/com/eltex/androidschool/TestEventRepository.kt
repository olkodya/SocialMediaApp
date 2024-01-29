package com.eltex.androidschool

import com.eltex.androidschool.model.Event
import com.eltex.androidschool.repository.EventRepository

interface TestEventRepository : EventRepository {
    override suspend fun getEvents(): List<Event> = error("Not implemented")
    override suspend fun likeById(id: Long): Event = error("Not implemented")

    override suspend fun participateById(id: Long): Event = error("Not implemented")

    override suspend fun unLikeById(id: Long): Event = error("Not implemented")

    override suspend fun unParticipateById(id: Long): Event = error("Not implemented")

    override suspend fun saveEvent(id: Long, content: String, datetime: String): Event =
        error("Not implemented")

    override suspend fun deleteById(id: Long): Unit = error("Not implemented")

}