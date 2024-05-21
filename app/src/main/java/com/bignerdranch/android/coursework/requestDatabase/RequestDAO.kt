package com.bignerdranch.android.coursework.requestDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.UUID

@Dao
interface RequestDAO {
    @Query("SELECT * FROM request")
    fun getRequests(): LiveData<List<Request>>
    @Query("SELECT * FROM request WHERE id=(:id)")
    fun getRequest(id: UUID): LiveData<Request?>
    @Insert
    fun addRequest(request: Request)
    @Query("DELETE FROM request WHERE text=(:text)")
    fun deleteRequest(text: String)
    @Query("SELECT COUNT(*) FROM request")
    fun countRequests(): Int

}