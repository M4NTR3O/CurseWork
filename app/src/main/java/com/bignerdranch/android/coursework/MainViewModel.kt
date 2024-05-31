package com.bignerdranch.android.coursework

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.bignerdranch.android.coursework.data.Repository
import com.bignerdranch.android.coursework.data.database.entities.FavoritesEntity
import com.bignerdranch.android.coursework.data.database.entities.RecipesEntity
import com.bignerdranch.android.coursework.data.network.SpoonacularResponse
import com.bignerdranch.android.coursework.models.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "MainViewModel"

class MainViewModel(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** Room Database */

    val readRecipe: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavoriteRecipe: LiveData<List<FavoritesEntity>> =
        repository.local.readFavoriteRecipes().asLiveData()

    fun insertRecipes(recipesEntity: RecipesEntity) = repository.local.insertRecipes(recipesEntity)

    fun insertFavoriteRecipes(favoritesEntity: FavoritesEntity) {
        repository.local.insertFavoriteRecipes(favoritesEntity)
    }


    fun deleteFavoriteRecipes(favoritesEntity: FavoritesEntity) {
        repository.local.deleteFavoriteRecipes(favoritesEntity)
    }

    fun deleteAllFavoriteRecipes() {
        repository.local.deleteAllFavoriteRecipes()
    }


    /** Retrofit */
    var recipeResponse: MutableLiveData<NetworkResult<SpoonacularResponse>> = MutableLiveData()
    var searchedRecipesResponse: MutableLiveData<NetworkResult<SpoonacularResponse>> = MutableLiveData()

    fun getRecipes(queries: String) = getRecipesSafeCall(queries)

    fun searchRecipes(searchQuery: String) = searchRecipesSafeCall(searchQuery)

    private fun searchRecipesSafeCall(searchQuery: String) {
        searchedRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.searchRecipes(searchQuery)
                searchedRecipesResponse.value = handleFoodRecipeResponse(response)!!
            } catch (e: Exception) {
                searchedRecipesResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            searchedRecipesResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun getRecipesSafeCall(queries: String) {
        recipeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                recipeResponse.value = handleFoodRecipeResponse(response)!!
                val foodRecipe = recipeResponse.value!!.data
                if (foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }
            } catch (e: Exception) {
                recipeResponse.value = NetworkResult.Error("Recipes not found")
            }
        } else {
            recipeResponse.value = NetworkResult.Error("No Internet Connection")
        }
    }

    private fun offlineCacheRecipes(foodRecipe: SpoonacularResponse) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun handleFoodRecipeResponse(call: Call<SpoonacularResponse>): NetworkResult<SpoonacularResponse>? {
        var network : NetworkResult<SpoonacularResponse>? = NetworkResult.Error("Start Function")
        call.enqueue(object : Callback<SpoonacularResponse>{
            override fun onFailure(call: Call<SpoonacularResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch recipes", t)
            }
                    override fun onResponse(
                call: Call<SpoonacularResponse>,
                response: Response<SpoonacularResponse>
            ) {
                Log.d(TAG, "Response received = ${response.body()}")
                    when {
                        response.message().toString().contains("timeout") -> {
                            network = NetworkResult.Error("Timeout")
                        }
                        response.code() == 402 -> {
                            network = NetworkResult.Error("API Key Limited.")
                        }
                        response.body()!!.results.isNullOrEmpty() -> {
                            network = NetworkResult.Error("Recipes not found.")
                        }
                        response.isSuccessful -> {
                            val foodRecipes = response.body()
                            network = NetworkResult.Success(foodRecipes!!)
                        }
                        else -> {
                            network = NetworkResult.Error(response.message())
                        }
                    }
            }
        })
        return network
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager


        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}