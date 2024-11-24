package com.bignerdranch.android.coursework.requestDatabase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
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

    suspend fun getRequestText(text: String): Request? = requestDao.getRequestText(text)

    fun addRequest(request: Request){
        executor.execute {
            requestDao.addRequest(request)
        }
    }
    fun updateRequest(request: Request){
        executor.execute {
            requestDao.updateRequest(request.id, request.date)
        }
    }

    fun getRequestName(text: String): LiveData<Request?> = requestDao.getRequestName(text)
    fun updateRequest(request: Request){
        executor.execute {
            requestDao.updateRequest(request.text, request.date)
        }
    }

    fun deleteRequest(request: Request){
        executor.execute {
            requestDao.deleteRequest(request.id)
        }
    }

    fun deleteRequestCount(){
        executor.execute(){
            requestDao.deleteRequestCount()
        }
    }

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