package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Event
import com.eltex.androidschool.model.FileModel

interface EventRepository {

    suspend fun getLatest(count: Int): List<Event>
    suspend fun getBefore(id: Long, count: Int): List<Event>
    suspend fun likeById(id: Long): Event
    suspend fun participateById(id: Long): Event
    suspend fun unLikeById(id: Long): Event
    suspend fun unParticipateById(id: Long): Event
    suspend fun saveEvent(id: Long, content: String, datetime: String, fileModel: FileModel?): Event
    suspend fun deleteById(id: Long)
}