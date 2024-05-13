package com.bignerdranch.android.coursework

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.coursework.requestDatabase.Request

class RequestListViewModel: ViewModel() {
    private var requestRepository = RequestRepository.get()
    val requestListLiveData = requestRepository.getRequests()

    fun addRequest(request: Request){
        requestRepository.addRequest(request)
        /*requestRepository.deleteRequest()*/
    }
}