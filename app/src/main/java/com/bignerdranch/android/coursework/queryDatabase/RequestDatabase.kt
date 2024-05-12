package com.bignerdranch.android.coursework.queryDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Request::class], version = 1)
@TypeConverters()
abstract class RequestDatabase: RoomDatabase() {
    abstract fun requestDao(): RequestDAO
}