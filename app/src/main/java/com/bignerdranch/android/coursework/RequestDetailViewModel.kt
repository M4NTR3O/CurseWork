package com.bignerdranch.android.coursework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.bignerdranch.android.coursework.requestDatabase.Request
import java.util.UUID

class RequestDetailViewModel(): ViewModel() {
    private val requestRepository = RequestRepository.get()
    private val requestIdLiveData = MutableLiveData<UUID>()
    var requestLiveData: LiveData<Request?> =
        requestIdLiveData.switchMap { requestId ->
            requestRepository.getRequest(requestId)
        }
    fun loadRequest(requestId: UUID) {
        requestIdLiveData.value = requestId
    }
}