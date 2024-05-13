package com.bignerdranch.android.coursework.requestDatabase

import androidx.room.TypeConverter
import java.util.UUID

class RequestTypeConverters {
    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }
}