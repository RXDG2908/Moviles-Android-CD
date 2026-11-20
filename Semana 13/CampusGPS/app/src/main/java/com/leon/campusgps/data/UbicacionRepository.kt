// Contrato para obtener la posición actual del celular.
package com.leon.campusgps.data

import com.leon.campusgps.model.Posicion
import kotlinx.coroutines.flow.Flow

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>

    // Emite una posición nueva cada vez que el celular se mueve; se detiene al cancelar
    fun seguirPosicion(): Flow<Posicion>
    fun playServicesDisponible(): Boolean
}

// Se usa cuando el GPS está apagado o el celular no logra ubicarse
class UbicacionNoDisponibleException : Exception("No se pudo obtener la ubicación")
