package com.bignerdranch.android.coursework.models

import com.google.gson.annotations.SerializedName

data class FoodRecipe (
    @SerializedName("results")
    var results: List<Result>
)