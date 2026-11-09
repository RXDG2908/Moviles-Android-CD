package com.tecsup.fortnite.data.repository

import com.tecsup.fortnite.data.local.ItemDao
import com.tecsup.fortnite.data.mapper.aEntity
import com.tecsup.fortnite.data.mapper.aModelo
import com.tecsup.fortnite.data.mapper.aModeloDetalle
import com.tecsup.fortnite.data.remote.ApiService
import com.tecsup.fortnite.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao
) : ItemRepository {

    override val items: Flow<List<Item>> = dao.todos().map { lista -> lista.map { it.aModelo() } }
    override val favoritos: Flow<List<Item>> = dao.favoritos().map { lista -> lista.map { it.aModelo() } }

    override suspend fun refrescar(): Result<Int> = try {
        // Si se insertan sin cuidado, se pierden los favoritos. Se conservan aquí:
        val favoritos = dao.idsFavoritos().toSet()
        val respuesta = api.listar()

        val nuevos = respuesta.data.entries
            // Solo cosméticos: se descartan pistas musicales, instrumentos y autos
            .filter { !it.brItems.isNullOrEmpty() }
            .filter { it.tracks.isNullOrEmpty() && it.instruments.isNullOrEmpty() && it.cars.isNullOrEmpty() }
            // Primero los que se venden solos, para que su precio no sea el de un lote
            .sortedBy { it.brItems!!.size }
            .map { it.aEntity() }
            .distinctBy { it.id }
            .map { it.copy(esFavorito = it.id in favoritos) }

        dao.reemplazarTienda(nuevos)
        Result.success(nuevos.size)
    } catch (e: IOException) { // sin internet
        Result.failure(e)
    } catch (e: HttpException) { // 404, 500...
        Result.failure(e)
    } catch (e: SerializationException) { // la API cambió el formato
        Result.failure(e)
    }

    override suspend fun obtenerDetalle(id: String): Result<Item> {
        val base = dao.buscar(id)?.aModelo()
        return try {
            Result.success(api.detalle(id).data.aModeloDetalle(base))
        } catch (e: IOException) {
            // Sin internet se muestra lo que hay guardado
            if (base != null) Result.success(base) else Result.failure(e)
        } catch (e: HttpException) {
            if (base != null) Result.success(base) else Result.failure(e)
        } catch (e: SerializationException) {
            if (base != null) Result.success(base) else Result.failure(e)
        }
    }

    override suspend fun alternarFavorito(id: String) = dao.alternarFavorito(id)
    override suspend fun cantidadGuardada() = dao.contar()
    override suspend fun cantidadFavoritos() = dao.contarFavoritos()
    override suspend fun limpiarCache() = dao.borrarTodo()
}
