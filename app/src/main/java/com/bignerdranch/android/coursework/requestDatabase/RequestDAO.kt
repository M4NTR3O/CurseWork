package com.bignerdranch.android.coursework.requestDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.UUID

@Dao
interface RequestDAO {
    @Query("SELECT * FROM request ORDER BY date DESC LIMIT 10")
    fun getRequests(): LiveData<List<Request>>
    @Query("SELECT * FROM request WHERE id=(:id)")
    fun getRequest(id: UUID): LiveData<Request?>
    @Query("UPDATE request SET date=(:date) WHERE id=(:id)")
    fun updateRequest(id: UUID, date: Long)
    @Query("SELECT * FROM request WHERE text=(:text) /*ORDER BY date ASC LIMIT 1*/")
    suspend fun getRequestText(text: String): Request?
    @Insert
    fun addRequest(request: Request)
    @Query("DELETE FROM request WHERE id IN (SELECT id FROM request LIMIT 1) AND (SELECT COUNT(*) FROM request) > 9")
    fun deleteRequestCount()

    @Query("DELETE FROM request WHERE id=(:id)")
    fun deleteRequest(id: UUID)

    @Query("SELECT COUNT(*) FROM request")
    fun countRequest(): Int
}