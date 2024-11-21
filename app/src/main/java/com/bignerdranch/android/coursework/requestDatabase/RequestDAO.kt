package com.bignerdranch.android.coursework.requestDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.Date
import java.util.UUID

@Dao
interface RequestDAO {
    @Query("SELECT * FROM request ORDER BY date DESC LIMIT 10")
    fun getRequests(): LiveData<List<Request>>
    @Query("SELECT * FROM request WHERE id=(:id)")
    fun getRequest(id: UUID): LiveData<Request?>
    @Query("SELECT * FROM request WHERE text=(:text)")
    fun getRequestName(text: String): LiveData<Request?>
    @Query("UPDATE Request SET date=(:date) WHERE text=(:text)")
    fun updateRequest(text: String, date: Long)
    @Insert
    fun addRequest(request: Request)
    @Query("DELETE FROM request WHERE id IN (SELECT id FROM request LIMIT 1) AND (SELECT COUNT(*) FROM request) > 9")
    fun deleteRequestCount()

    @Query("DELETE FROM request WHERE id=(:id)")
    fun deleteRequest(id: UUID)

    @Query("SELECT COUNT(*) FROM request")
    fun countRequest(): Int
}