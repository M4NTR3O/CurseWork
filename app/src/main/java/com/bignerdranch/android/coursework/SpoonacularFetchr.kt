package com.bignerdranch.android.coursework

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.coursework.models.FoodRecipe
import com.bignerdranch.android.coursework.models.Result
import com.bignerdranch.android.coursework.network.FoodRecipeApi
import com.bignerdranch.android.coursework.network.RecipeInterceptor
import com.bignerdranch.android.coursework.network.SpoonacularResponse
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "SpoonacularFetchr"

class SpoonacularFetchr {

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

    fun searchRecipesRequest(query: String): Call<SpoonacularResponse> {
        return foodRecipeApi.getRecipes(query)
    }

    fun searchRecipes(query: String): LiveData<List<Result>> {
        if (query == ""){
            return fetchRecipes(searchRecipesRequest("pasta"))
        }
        return fetchRecipes(searchRecipesRequest(query))
    }


    fun fetchRecipes(spoonacularRequest: Call<SpoonacularResponse>): LiveData<List<Result>>{
        val responseLiveData: MutableLiveData<List<Result>> = MutableLiveData()
        spoonacularRequest.enqueue(object : Callback<SpoonacularResponse> {
            override fun onFailure(call:
                                   Call<SpoonacularResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch recipes", t)
            }
            override fun onResponse(
                call: Call<SpoonacularResponse>,
                response: Response<SpoonacularResponse>
            ) {
                Log.d(TAG, "Response received = ${response.body()}")
                val spoonacularResponse: SpoonacularResponse? = response.body()
                var recipeItems:
                        List<Result> = spoonacularResponse?.results?: mutableListOf()
                responseLiveData.value = recipeItems
            }
        })
        return responseLiveData

    }
}