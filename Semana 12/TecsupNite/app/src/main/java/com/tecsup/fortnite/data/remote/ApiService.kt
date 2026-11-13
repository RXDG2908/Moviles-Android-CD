// Endpoints de la API de Fortnite (tienda y detalle).
package com.tecsup.fortnite.data.remote

import com.tecsup.fortnite.data.remote.dto.DetalleResponse
import com.tecsup.fortnite.data.remote.dto.TiendaResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Primer endpoint: la tienda de hoy
    @GET("v2/shop")
    suspend fun listar(@Query("language") idioma: String = "es"): TiendaResponse

    // Segundo endpoint: un cosmético por su id
    @GET("v2/cosmetics/br/{id}")
    suspend fun detalle(
        @Path("id") id: String,
        @Query("language") idioma: String = "es"
    ): DetalleResponse
}
