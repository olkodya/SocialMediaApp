package com.eltex.androidschool.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.eltex.androidschool.entity.EventEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface EventDao {
    @Query("SELECT*FROM Events ORDER BY id DESC")
    fun getAll(): Flow<List<EventEntity>>

    @Upsert
    fun save(event: EventEntity): Long

    @Query(
        """
        UPDATE Events SET 
        likedByMe = CASE WHEN likedByMe = 1 THEN 0 ELSE 1 END
        WHERE id = :eventId
        
    """
    )
    fun likeById(eventId: Long)

    @Query(
        """
        UPDATE Events SET 
        participatedByMe = CASE WHEN participatedByMe = 1 THEN 0 ELSE 1 END
        WHERE id = :eventId
        
    """
    )
    fun participateById(eventId: Long)

    @Query("DELETE FROM Events WHERE id = :eventId")
    fun deleteById(eventId: Long)

    @Query(
        """
        UPDATE Events SET 
        content = :content WHERE id = :eventId
        
    """
    )
    fun editById(eventId: Long, content: String)
}
