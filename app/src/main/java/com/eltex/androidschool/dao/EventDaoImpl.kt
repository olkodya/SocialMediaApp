package com.eltex.androidschool.dao

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
import com.eltex.androidschool.model.Event
import com.eltex.androidschool.utils.getBooleanOrThrow
import com.eltex.androidschool.utils.getEventTypeOrThrow
import com.eltex.androidschool.utils.getLongOrThrow
import com.eltex.androidschool.utils.getStringOrThrow

class EventDaoImpl(private val db: SQLiteDatabase) : EventDao {
    override fun getAll(): List<Event> =
        db.query(
            EventsTable.TABLE_NAME,
            EventsTable.allColumns,
            null,
            null,
            null,
            null,
            "${EventsTable.ID} DESC",
            null
        ).use { cursor ->
            val result = mutableListOf<Event>()

            while (cursor.moveToNext()) {
                result += cursor.getEvent()
            }
            result
        }

    override fun save(event: Event): Event {
        val contentValues = contentValuesOf(
            EventsTable.AUTHOR to event.author,
            EventsTable.CONTENT to event.content,
            EventsTable.PUBLISHED to event.published,
            EventsTable.LIKED_BY_ME to event.likedByMe,
            EventsTable.PARTICIPATED_BY_ME to event.participatedByMe,
            EventsTable.DATETIME to event.datetime,
            EventsTable.TYPE to event.type.name,
            EventsTable.LINK to event.link
        )

        if (event.id != 0L) {
            contentValues.put(EventsTable.ID, event.id)
        }
        val id = db.replace(EventsTable.TABLE_NAME, null, contentValues)
        return getEventById(id)
    }

    private fun getEventById(id: Long): Event =
        db.query(
            EventsTable.TABLE_NAME,
            EventsTable.allColumns,
            "${EventsTable.ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null,
        ).use { cursor ->
            cursor.moveToNext()
            cursor.getEvent()
        }

    override fun likeById(eventId: Long): Event {
        db.execSQL(
            """
            UPDATE ${EventsTable.TABLE_NAME} SET 
                likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END 
            WHERE id = ?;
        """.trimIndent(), arrayOf(eventId.toString())
        )
        return getEventById(eventId)
    }

    override fun participateById(eventId: Long): Event {
        db.execSQL(
            """
            UPDATE ${EventsTable.TABLE_NAME} SET 
                participatedByMe = CASE WHEN participatedByMe THEN 0 ELSE 1 END 
            WHERE id = ?;
        """.trimIndent(), arrayOf(eventId.toString())
        )
        return getEventById(eventId)
    }


    override fun deleteById(eventId: Long) {
        db.delete(EventsTable.TABLE_NAME, "${EventsTable.ID} = ?", arrayOf(eventId.toString()))
    }


    private fun Cursor.getEvent(): Event =
        Event(
            id = getLongOrThrow(EventsTable.ID),
            content = getStringOrThrow(EventsTable.CONTENT),
            author = getStringOrThrow(EventsTable.AUTHOR),
            published = getStringOrThrow(EventsTable.PUBLISHED),
            likedByMe = getBooleanOrThrow(EventsTable.LIKED_BY_ME),
            type = getEventTypeOrThrow(EventsTable.TYPE),
            participatedByMe = getBooleanOrThrow(EventsTable.PARTICIPATED_BY_ME),
            link = getStringOrThrow(EventsTable.LINK),
        )

    override fun editById(eventId: Long, content: String): Event {
        db.execSQL(
            """
            UPDATE ${EventsTable.TABLE_NAME} SET 
                ${EventsTable.CONTENT} =  '$content'  WHERE id = ?;
        """.trimIndent(), arrayOf(eventId.toString())
        )
        return getEventById(eventId)
    }

}