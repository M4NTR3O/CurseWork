package com.bignerdranch.android.coursework.requestDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

@Entity
data class Request(@PrimaryKey val id: UUID = UUID.randomUUID(),
                   var text: String = "",
                   var date: Long = Date().time) {
}