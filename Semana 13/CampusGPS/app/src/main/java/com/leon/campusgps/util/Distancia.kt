// Distancia en metros entre la posición del usuario y un lugar.
package com.leon.campusgps.util

import android.location.Location
import com.leon.campusgps.model.Lugar
import com.leon.campusgps.model.Posicion

fun distanciaMetros(desde: Posicion, hasta: Lugar): Float {
    // distanceBetween guarda el resultado en la primera casilla del arreglo
    val resultado = FloatArray(1)
    Location.distanceBetween(
        desde.latitud, desde.longitud,
        hasta.latitud, hasta.longitud,
        resultado
    )
    return resultado[0]
}
