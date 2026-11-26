// Repository de publicaciones: el ViewModel solo conoce la interface y recibe Result.
package com.leon.murocampus.data

import com.leon.murocampus.data.remote.PostApi
import com.leon.murocampus.model.Comentario
import com.leon.murocampus.model.NuevoPost
import com.leon.murocampus.model.Post
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

interface PostRepository {
    suspend fun listar(): Result<List<Post>>
    suspend fun detalle(id: Int): Result<Post>
    suspend fun comentarios(id: Int): Result<List<Comentario>>
    suspend fun porUsuario(userId: Int): Result<List<Post>>
    suspend fun crear(nuevo: NuevoPost): Result<Post>
    suspend fun borrar(id: Int): Result<Unit>
}

// Implementación que usa Retrofit; ninguna excepción sale de aquí
class PostRepositoryRemote(private val api: PostApi) : PostRepository {

    override suspend fun listar() = llamar { api.listar() }

    override suspend fun detalle(id: Int) = llamar { api.detalle(id) }

    override suspend fun comentarios(id: Int) = llamar { api.comentarios(id) }

    override suspend fun porUsuario(userId: Int) = llamar { api.porUsuario(userId) }

    override suspend fun crear(nuevo: NuevoPost) = llamar { api.crear(nuevo) }

    // Con Response, un 404 o 500 no lanza solo: se revisa el código y se convierte en error
    override suspend fun borrar(id: Int) = llamar {
        val respuesta = api.borrar(id)
        if (!respuesta.isSuccessful) throw HttpException(respuesta)
    }

    // Un solo try/catch para las cinco llamadas: éxito o fallo dentro de un Result
    private suspend fun <T> llamar(bloque: suspend () -> T): Result<T> =
        try {
            Result.success(bloque())
        } catch (e: CancellationException) {
            throw e // la cancelación no es un error: se deja pasar
        } catch (e: Exception) {
            Result.failure(e)
        }
}
