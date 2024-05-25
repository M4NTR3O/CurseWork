package com.bignerdranch.android.coursework.network

import com.bignerdranch.android.coursework.models.FoodRecipe
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipeApi {

    @GET("/recipes/complexSearch?apiKey=d981038db4b741f6b818fe7813c22e70")
    fun getRecipes(@Query("query") query: String) : Call<SpoonacularResponse>

}