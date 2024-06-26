package com.bignerdranch.android.coursework.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@Entity
data class Result(
    /*@SerializedName("aggregateLikes")*/
    val aggregateLikes: Int,
   /* @SerializedName("cheap")*/
    val cheap: Boolean,
    /*@SerializedName("dairyFree")*/
    val dairyFree: Boolean,
    /*@SerializedName("extendedIngredients")*/
    val extendedIngredients: @RawValue List<ExtendedIngredient>,
    /*@SerializedName("glutenFree")*/
    val glutenFree: Boolean,
    /*@SerializedName("readyInMinutes")*/
    val readyInMinutes: Int,
    /*@SerializedName("sourceName")*/
    val sourceName: String?,
    /*@SerializedName("sourceUrl")*/
    val sourceUrl: String,
    /*@SerializedName("summary")*/
    val summary: String,
    /*@SerializedName("vegan")*/
    val vegan: Boolean,
    /*@SerializedName("vegetarian")*/
    val vegetarian: Boolean,
    /*@SerializedName("veryHealthy")*/
    val veryHealthy: Boolean,
    /*@SerializedName("id")*/
    val id: String = "",
    /*@SerializedName("image")*/
    val image: String = "",
    /*@SerializedName("title")*/
    val title: String = ""
): Parcelable{
}