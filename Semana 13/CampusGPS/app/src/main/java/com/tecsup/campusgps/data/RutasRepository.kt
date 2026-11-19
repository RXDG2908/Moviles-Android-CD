// Contrato para pedir una ruta a pie entre dos puntos.
package com.tecsup.campusgps.data

import com.tecsup.campusgps.model.Posicion
import com.tecsup.campusgps.model.Ruta

interface RutasRepository {
    suspend fun rutaAPie(origen: Posicion, destino: Posicion): Result<Ruta>
}
