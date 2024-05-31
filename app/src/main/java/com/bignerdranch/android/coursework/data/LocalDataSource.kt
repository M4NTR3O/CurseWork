package com.bignerdranch.android.coursework.data

import com.bignerdranch.android.coursework.data.database.RecipesDao
import com.bignerdranch.android.coursework.data.database.entities.FavoritesEntity
import com.bignerdranch.android.coursework.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val recipesDao: RecipesDao) {

    fun readRecipes(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }


    fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }

    fun readFavoriteRecipes():Flow<List<FavoritesEntity>> {
        return recipesDao.readFavoriteRecipes()
    }

    fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity){
        recipesDao.insertFavoriteRecipe(favoritesEntity)
    }

    fun deleteFavoriteRecipes(favoritesEntity: FavoritesEntity){
        recipesDao.deleteFavoriteRecipe(favoritesEntity)
    }

    fun deleteAllFavoriteRecipes(){
        recipesDao.deleteAllFavoriteRecipe()
    }
}