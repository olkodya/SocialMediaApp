package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Event
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    fun getEvent(): Flow<List<Event>>
    fun likeById(id: Long)
    fun participateById(id: Long)
    abstract fun addEvent(content: String)
    abstract fun deleteById(id: Long)
    abstract fun editById(id: Long, content: String)
}