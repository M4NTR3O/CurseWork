package com.bignerdranch.android.coursework.requestDatabase

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Request::class],
    version = 2,
    exportSchema = false)
@TypeConverters()
abstract class RequestDatabase: RoomDatabase() {
    abstract fun requestDao(): RequestDAO
}