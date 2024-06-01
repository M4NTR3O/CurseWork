package com.bignerdranch.android.coursework.data

import android.content.Context
import androidx.room.Room
import com.bignerdranch.android.coursework.data.database.RecipesDatabase
import com.bignerdranch.android.coursework.data.network.FoodRecipeApi
import com.bignerdranch.android.coursework.data.network.RecipeInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val DATABASE_NAME = "recipes_database"
class Repository private constructor(context: Context) {

    private val foodRecipeApi: FoodRecipeApi
    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(RecipeInterceptor())
            .build()
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        foodRecipeApi = retrofit.create(FoodRecipeApi::class.java)
    }
    private val database : RecipesDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            RecipesDatabase::class.java,
            DATABASE_NAME
        ).build()

    val remote = RemoteDataSource(foodRecipeApi)
    val local = LocalDataSource(database.recipesDao())
    companion object {
        private var INSTANCE: Repository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = Repository(context)
            }
        }
        fun get(): Repository {
            return INSTANCE ?:
            throw
            IllegalStateException("CrimeRepository must be initialized")
        }
    }
}