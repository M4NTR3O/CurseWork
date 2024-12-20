package com.bignerdranch.android.coursework

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.coursework.requestDatabase.Request
import com.bignerdranch.android.coursework.requestDatabase.RequestRepository

class RequestListViewModel: ViewModel() {
    private var requestRepository = RequestRepository.get()
    val requestListLiveData = requestRepository.getRequests()

    fun addRequest(request: Request){
        requestRepository.addRequest(request)
    }


    suspend fun getRequest(request: Request): Request?{
        return requestRepository.getRequestText(request.text)
    }

    fun deleteRequestCount(){
        requestRepository.deleteRequestCount()
    }

    fun deleteRequest(request: Request){
        requestRepository.deleteRequest(request)
    }
}