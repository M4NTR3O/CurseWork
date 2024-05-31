package com.bignerdranch.android.coursework.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bignerdranch.android.coursework.data.network.SpoonacularResponse


@Entity(tableName = "recipes_table")
class RecipesEntity(var foodRecipe : SpoonacularResponse) {
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0
}