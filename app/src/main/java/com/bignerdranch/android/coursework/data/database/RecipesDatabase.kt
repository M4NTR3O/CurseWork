package com.bignerdranch.android.coursework.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bignerdranch.android.coursework.data.database.entities.FavoritesEntity
import com.bignerdranch.android.coursework.data.database.entities.RecipesEntity

@Database(
    entities = [RecipesEntity::class, FavoritesEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(RecipesTypeConvertor::class)
abstract class RecipesDatabase : RoomDatabase(){

    abstract fun recipesDao() : RecipesDao


}