package com.eltex.androidschool.repository

import com.eltex.androidschool.api.EventsApi
import com.eltex.androidschool.model.Event
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

class NetworkEventRepository(
    private val api: EventsApi
) : EventRepository {

    override fun getEvents(): Single<List<Event>> = api.getAll()

    override fun likeById(id: Long): Single<Event> = api.like(id)

    override fun participateById(id: Long): Single<Event> = api.participate(id)

    override fun unLikeById(id: Long): Single<Event> = api.unlike(id)

    override fun unParticipateById(id: Long): Single<Event> = api.unparticipate(id)

    override fun saveEvent(id: Long, content: String, datetime: String): Single<Event> =
        api.save(Event(id, content, datetime))

    override fun deleteById(id: Long): Completable = api.delete(id)

}