package com.tecsup.fortnite.data.repository

import com.tecsup.fortnite.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    val items: Flow<List<Item>>      // sale de Room
    val favoritos: Flow<List<Item>>
    suspend fun refrescar(): Result<Int> // Retrofit -> Room; devuelve cuántos guardó
    suspend fun obtenerDetalle(id: String): Result<Item>
    suspend fun alternarFavorito(id: String)
    suspend fun cantidadGuardada(): Int
    suspend fun cantidadFavoritos(): Int
    suspend fun limpiarCache()
}
