// Contrato para obtener los lugares del campus.
package com.leon.campusgps.data

import com.leon.campusgps.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}
