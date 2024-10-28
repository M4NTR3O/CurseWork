package com.bignerdranch.android.coursework.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bignerdranch.android.coursework.models.Result

@Entity(tableName = "favorite_recipes_table")
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id : Int,
    var result: Result
)