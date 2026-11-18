// Contrato para obtener la posición actual del celular.
package com.tecsup.campusgps.data

import com.tecsup.campusgps.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
    fun playServicesDisponible(): Boolean
}

// Se usa cuando el GPS está apagado o el celular no logra ubicarse
class UbicacionNoDisponibleException : Exception("No se pudo obtener la ubicación")
