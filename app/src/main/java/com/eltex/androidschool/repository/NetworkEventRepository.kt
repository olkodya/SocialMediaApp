package com.eltex.androidschool.repository

import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.model.Event

class NetworkEventRepository(
    private val api: EventsApi
) : EventRepository {
    override suspend fun getLatest(count: Int): List<Event> = api.getLatest(count)

    override suspend fun getBefore(id: Long, count: Int): List<Event> =
        api.getBefore(id, count)

    override suspend fun likeById(id: Long): Event = api.like(id)

    override suspend fun participateById(id: Long): Event = api.participate(id)

    override suspend fun unLikeById(id: Long): Event = api.unlike(id)

    override suspend fun unParticipateById(id: Long): Event = api.unparticipate(id)

    override suspend fun saveEvent(id: Long, content: String, datetime: String): Event =
        api.save(Event(id, content, datetime))

    override suspend fun deleteById(id: Long) = api.delete(id)

}