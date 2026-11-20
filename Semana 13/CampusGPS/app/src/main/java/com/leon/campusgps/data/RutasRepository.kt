// Contrato para pedir una ruta a pie entre dos puntos.
package com.leon.campusgps.data

import com.leon.campusgps.model.Posicion
import com.leon.campusgps.model.Ruta

interface RutasRepository {
    suspend fun rutaAPie(origen: Posicion, destino: Posicion): Result<Ruta>
}
