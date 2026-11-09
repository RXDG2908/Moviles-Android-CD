package com.tecsup.fortnite.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM items ORDER BY titulo")
    fun todos(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE esFavorito = 1 ORDER BY titulo")
    fun favoritos(): Flow<List<ItemEntity>>

    @Query("SELECT id FROM items WHERE esFavorito = 1")
    suspend fun idsFavoritos(): List<String>

    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun buscar(id: String): ItemEntity?

    @Query("SELECT COUNT(*) FROM items")
    suspend fun contar(): Int

    @Query("SELECT COUNT(*) FROM items WHERE esFavorito = 1")
    suspend fun contarFavoritos(): Int

    @Upsert
    suspend fun guardarTodos(items: List<ItemEntity>)

    @Query("UPDATE items SET esFavorito = NOT esFavorito WHERE id = :id")
    suspend fun alternarFavorito(id: String)

    // Quita lo que ya no está en la tienda, pero deja los favoritos
    @Query("DELETE FROM items WHERE esFavorito = 0")
    suspend fun borrarNoFavoritos()

    // Todo junto, para que la lista no se vea vacía a mitad del cambio
    @Transaction
    suspend fun reemplazarTienda(items: List<ItemEntity>) {
        borrarNoFavoritos()
        guardarTodos(items)
    }

    @Query("DELETE FROM items")
    suspend fun borrarTodo()
}
