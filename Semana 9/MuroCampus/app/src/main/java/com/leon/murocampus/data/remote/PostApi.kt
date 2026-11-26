// Interface de Retrofit: cada función es una llamada al servicio. Sin código dentro.
package com.leon.murocampus.data.remote

import com.leon.murocampus.model.Comentario
import com.leon.murocampus.model.NuevoPost
import com.leon.murocampus.model.Post
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// Las rutas no empiezan con "/" para que se sumen a la URL base
interface PostApi {

    // GET posts: las 100 publicaciones
    @GET("posts")
    suspend fun listar(): List<Post>

    // GET posts/1: @Path reemplaza {id} dentro de la ruta
    @GET("posts/{id}")
    suspend fun detalle(@Path("id") id: Int): Post

    // GET posts/1/comments
    @GET("posts/{id}/comments")
    suspend fun comentarios(@Path("id") id: Int): List<Comentario>

    // GET posts?userId=2: @Query agrega el filtro al final de la URL
    @GET("posts")
    suspend fun porUsuario(@Query("userId") userId: Int): List<Post>

    // POST posts: @Body manda el objeto como JSON en el cuerpo
    @POST("posts")
    suspend fun crear(@Body nuevo: NuevoPost): Post

    // DELETE posts/1: la respuesta llega vacía; Response deja revisar el código HTTP
    @DELETE("posts/{id}")
    suspend fun borrar(@Path("id") id: Int): Response<Unit>
}
