package com.eltex.androidschool.repository

import com.eltex.androidschool.model.Event
import com.eltex.androidschool.utils.Callback

interface EventRepository {

    fun getEvents(callback: Callback<List<Event>>)
    fun likeById(id: Long, callback: Callback<Event>, likedByMe: Boolean)
    fun participateById(id: Long, callback: Callback<Event>, participatedByMe: Boolean)
    fun saveEvent(id: Long, content: String, callback: Callback<List<Event>>)
    fun deleteById(id: Long, callback: Callback<Unit>)
    fun editById(id: Long, content: String)
}