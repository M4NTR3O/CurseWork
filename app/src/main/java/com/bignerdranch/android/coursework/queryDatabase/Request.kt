package com.bignerdranch.android.coursework.queryDatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Request(@PrimaryKey val id: UUID = UUID.randomUUID(),
                   var text: String = "") {
}