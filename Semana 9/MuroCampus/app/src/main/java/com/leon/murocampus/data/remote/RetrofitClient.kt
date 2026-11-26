// Cliente de Retrofit: se crea una sola vez y lo usa el Repository.
package com.leon.murocampus.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {

    // ignoreUnknownKeys: si el JSON trae campos que no usamos, no falla
    private val json = Json { ignoreUnknownKeys = true }

    // OkHttp hace la conexión; el interceptor muestra en Logcat cada petición y su JSON
    // Agrega el encabezado Accept a todas las peticiones: le dice al servidor que queremos JSON
    private val encabezadoJson = Interceptor { cadena ->
        val peticion = cadena.request().newBuilder()
            .header("Accept", "application/json")
            .build()
        cadena.proceed(peticion)
    }

    private val cliente = OkHttpClient.Builder()
        // Primero el encabezado y después el registro, para que Logcat lo muestre
        .addInterceptor(encabezadoJson)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    // by lazy: se crea la primera vez que se usa y después se reutiliza
    val api: PostApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/") // termina con /
            .client(cliente)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(PostApi::class.java)
    }
}
