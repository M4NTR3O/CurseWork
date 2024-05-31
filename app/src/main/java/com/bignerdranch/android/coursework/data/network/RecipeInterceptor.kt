package com.bignerdranch.android.coursework.data.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

private const val API_KEY = "d981038db4b741f6b818fe7813c22e70"

class RecipeInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val newUrl: HttpUrl =
            originalRequest.url().newBuilder()
                /*.addQueryParameter("api_key", API_KEY)*/
                .addQueryParameter("number", "10")
                .addQueryParameter("addRecipeInformation", "true")
                .addQueryParameter("fillIngredients", "true")
                .build()
        val newRequest: Request =
            originalRequest.newBuilder()
                .url(newUrl)
                .build()
        return chain.proceed(newRequest)
    }
}