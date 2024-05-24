package com.bignerdranch.android.coursework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.bignerdranch.android.coursework.models.Result
import com.squareup.picasso.Transformation

class RecipeViewModel: ViewModel() {
    val recipeItemLiveData: LiveData<List<Result>>
    private val spoonacularFetchr = SpoonacularFetchr()
    private val mutableSearchTerm = MutableLiveData<String>()
    init {
        mutableSearchTerm.value = "pasta"
        recipeItemLiveData =
        mutableSearchTerm.switchMap { searchTerm ->
            spoonacularFetchr.searchRecipes(searchTerm)
        }

    }

    fun fetchRecipe(query: String = "") {
        mutableSearchTerm.value = query
    }

}