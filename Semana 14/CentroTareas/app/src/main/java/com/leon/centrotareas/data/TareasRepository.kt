// Contrato del Repository: el ViewModel solo conoce esta interface.
package com.leon.centrotareas.data

import com.leon.centrotareas.model.AvancePrimos
import kotlinx.coroutines.flow.Flow

interface TareasRepository {
    // Avance de la tarea simulada, de 0f a 1f
    fun tareaConProgreso(): Flow<Float>

    suspend fun contarPrimos(hasta: Int): Int
    // Igual que contarPrimos, pero va avisando el porcentaje
    fun contarPrimosConProgreso(hasta: Int): Flow<AvancePrimos>
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>
}
