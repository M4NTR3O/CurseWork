package com.bignerdranch.android.coursework

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bignerdranch.android.coursework.network.FoodRecipeApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "SpoonacularFetchr"

class SpoonacularFetchr {

    private val foodRecipeApi: FoodRecipeApi
    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        foodRecipeApi = retrofit.create(FoodRecipeApi::class.java)
    }

    fun getRecipes(): LiveData<String>{
        val responseLiveData: MutableLiveData<String> = MutableLiveData()
        val spoonacularRequest: Call<String> = foodRecipeApi.getRecipes()
        spoonacularRequest.enqueue(object : Callback<String> {
            override fun onFailure(call:
                                   Call<String>, t: Throwable) {
                Log.e(TAG, "Failed to fetch recipes", t)
            }
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                Log.d(TAG, "Response received")
                responseLiveData.value = response.body()
            }
        })
        return responseLiveData

    }
}