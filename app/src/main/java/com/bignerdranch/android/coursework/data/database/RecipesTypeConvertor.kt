package com.bignerdranch.android.coursework.data.database

import androidx.room.TypeConverter
import com.bignerdranch.android.coursework.data.network.SpoonacularResponse

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.bignerdranch.android.coursework.models.Result

class RecipesTypeConvertor {

    var gson = Gson()

    @TypeConverter
    fun foodRecipeToString(foodRecipe: SpoonacularResponse): String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): SpoonacularResponse {
        val listType = object : TypeToken<SpoonacularResponse>() {}.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun resultToString(result: Result): String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data: String): Result {
        val listType = object : TypeToken<Result>(){}.type
        return gson.fromJson(data, listType)
    }
}