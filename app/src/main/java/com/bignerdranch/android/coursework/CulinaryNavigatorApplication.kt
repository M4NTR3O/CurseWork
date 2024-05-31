package com.bignerdranch.android.coursework

import android.app.Application
import android.util.Log
import com.bignerdranch.android.coursework.data.Repository
import com.bignerdranch.android.coursework.requestDatabase.RequestRepository

private const val TAG = "Start"
class CulinaryNavigatorApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        RequestRepository.initialize(this)
        Log.d(TAG, RequestRepository.get().toString())
    }
}