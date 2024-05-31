package com.bignerdranch.android.coursework.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bignerdranch.android.coursework.data.database.entities.FavoritesEntity
import com.bignerdranch.android.coursework.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {
    // RecipesEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipes(recipesEntity: RecipesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

    // Favorite Entity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity)

    @Query("SELECT * FROM favorite_recipes_table ORDER BY id ASC")
    fun readFavoriteRecipes(): Flow<List<FavoritesEntity>>

    @Delete
    fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity)

    @Query("DELETE FROM favorite_recipes_table")
    fun deleteAllFavoriteRecipe()


}