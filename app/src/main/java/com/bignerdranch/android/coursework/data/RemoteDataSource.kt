package com.bignerdranch.android.coursework.data

import com.bignerdranch.android.coursework.data.network.FoodRecipeApi
import com.bignerdranch.android.coursework.data.network.SpoonacularResponse
import retrofit2.Call
import retrofit2.Response

class RemoteDataSource(
    private val foodRecipeApi: FoodRecipeApi
) {

    fun getRecipes(queries : String): Call<SpoonacularResponse> {
        return foodRecipeApi.getRecipes(queries)
    }

    fun searchRecipes(searchQuery : String) : Call<SpoonacularResponse>{
        return foodRecipeApi.searchRecipes(searchQuery)
    }
}