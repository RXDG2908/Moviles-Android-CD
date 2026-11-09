package com.tecsup.fortnite.data.remote

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object RetrofitClient {

    // La API trae muchos campos que no usamos, por eso se ignoran
    private val json = Json { ignoreUnknownKeys = true }

    val api: ApiService = Retrofit.Builder()
        .baseUrl("https://fortnite-api.com/")
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(ApiService::class.java)
}
