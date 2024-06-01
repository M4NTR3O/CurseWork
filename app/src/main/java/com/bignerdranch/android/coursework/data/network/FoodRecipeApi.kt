package com.bignerdranch.android.coursework.data.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodRecipeApi {

    @GET("/recipes/complexSearch?apiKey=c385209d65ef47a98f824b152c1204c7")
    fun getRecipes(@Query("query") query: String) : Call<SpoonacularResponse>

    @GET("/recipes/complexSearch?apiKey=c385209d65ef47a98f824b152c1204c7")
    fun searchRecipes(@Query("query") query: String) : Call<SpoonacularResponse>

}