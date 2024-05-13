package com.bignerdranch.android.coursework

import android.app.Application
import com.bignerdranch.android.coursework.requestDatabase.Request

class CulinaryNavigatorApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        RequestRepository.initialize(this)
    }
}