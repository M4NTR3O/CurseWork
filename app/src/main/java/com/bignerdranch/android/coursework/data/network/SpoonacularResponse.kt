package com.bignerdranch.android.coursework.data.network

import com.bignerdranch.android.coursework.models.Result
import com.google.gson.annotations.SerializedName

data class SpoonacularResponse (
    @SerializedName("results")
    var results: List<Result>
)