package com.bignerdranch.android.coursework

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import com.bignerdranch.android.coursework.queryDatabase.Request
import com.bignerdranch.android.coursework.queryDatabase.RequestDatabase
import java.util.UUID
import java.util.concurrent.Executors

private const val DATABASE_NAME = "request-database"
class RequestRepository private constructor(context: Context){
    private val database : RequestDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            RequestDatabase::class.java,
            DATABASE_NAME
        ).build()
    private val requestDao = database.requestDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getRequests(): LiveData<List<Request>> = requestDao.getRequests()

    fun getRequest(id: UUID): LiveData<Request?> = requestDao.getRequest(id)

    fun addRequest(request: Request){
        executor.execute {
            requestDao.addRequest(request)
        }
    }

    fun deleteRequest() = requestDao.deleteRequest()

    fun countRequests(): Int = requestDao.countRequests()

    companion object {
        private var INSTANCE: RequestRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = RequestRepository(context)
            }
        }
        fun get(): RequestRepository {
            return INSTANCE ?:
            throw
            IllegalStateException("RequestRepository must be initialized")
        }
    }
}