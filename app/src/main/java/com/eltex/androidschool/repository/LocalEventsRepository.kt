package com.eltex.androidschool.repository

import android.content.Context
import androidx.core.content.edit
import com.eltex.androidschool.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class LocalEventsRepository(private val context: Context) : EventRepository {
    private companion object {
        const val EVENT_FILE_NAME = "events.json"
        const val ID_KEY = "ID_KEY"
    }

    private var preferences = context.getSharedPreferences("events", Context.MODE_PRIVATE)

    private val state = MutableStateFlow(readEvents())

    private var nextId = readId()


    private fun readEvents(): List<Event> {
        val file = context.filesDir.resolve(EVENT_FILE_NAME)
        val eventsSerialized = if (file.exists()) {
            file.bufferedReader()
                .use {
                    it.readLine()
                }
        } else {
            null
        }
        return if (eventsSerialized != null) {
            Json.decodeFromString(eventsSerialized)
        } else {
            emptyList()
        }
    }

    private fun readId(): Long = preferences.getLong(ID_KEY, 0L)


    override fun getEvent(): Flow<List<Event>> = state.asStateFlow()

    override fun likeById(id: Long) {
        state.update { events ->
            events.map {
                if (id == it.id) {
                    it.copy(likedByMe = !it.likedByMe)
                } else {
                    it
                }
            }
        }
        sync()
    }


    override fun participateById(id: Long) {
        state.update { events ->
            events.map {
                if (id == it.id) {
                    it.copy(participatedByMe = !it.participatedByMe)
                } else {
                    it
                }
            }
        }
        sync()
    }

    override fun addEvent(content: String) {
        state.update { events ->
            buildList(events.size + 1) {
                add(Event(id = ++nextId, content = content, author = "Olga", published = "Now"))
                addAll(events)
            }
        }

        sync()
    }

    override fun deleteById(id: Long) {
        state.update { events ->
            events.filter {
                it.id != id
            }

        }
        sync()
    }

    override fun editById(id: Long, content: String) {
        state.update { events ->
            events.map {
                if (id == it.id) {
                    it.copy(content = content)
                } else {
                    it
                }
            }
        }
        sync()
    }

    private fun sync() {
        preferences.edit {
            putLong(ID_KEY, nextId)
        }

        context.filesDir.resolve(EVENT_FILE_NAME)
            .bufferedWriter()
            .use {
                it.write(Json.encodeToString(state.value))
            }


    }
}