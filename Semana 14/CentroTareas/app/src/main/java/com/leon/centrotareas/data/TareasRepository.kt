// Contrato del Repository: el ViewModel solo conoce esta interface.
package com.leon.centrotareas.data

import kotlinx.coroutines.flow.Flow

interface TareasRepository {
    // Avance de la tarea simulada, de 0f a 1f
    fun tareaConProgreso(): Flow<Float>

    suspend fun contarPrimos(hasta: Int): Int
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>
}
