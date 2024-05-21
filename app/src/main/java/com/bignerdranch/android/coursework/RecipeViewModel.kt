package com.bignerdranch.android.coursework

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.coursework.models.Result

class RecipeViewModel: ViewModel() {
    val recipeItemLiveData: LiveData<List<Result>>
    init {
        recipeItemLiveData = SpoonacularFetchr().getRecipes()
    }

}