package com.bignerdranch.android.coursework.data.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodRecipeApi {

    @GET("/recipes/complexSearch?apiKey=d981038db4b741f6b818fe7813c22e70")
    fun getRecipes(@Query("query") query: String) : Call<SpoonacularResponse>

    @GET("/recipes/complexSearch?apiKey=d981038db4b741f6b818fe7813c22e70")
    fun searchRecipes(@Query("query") query: String) : Call<SpoonacularResponse>

}