// Decodifica la polilínea comprimida que devuelve Google (formato "encoded polyline").
package com.leon.campusgps.util

import com.leon.campusgps.model.Posicion

fun decodificarPolilinea(codigo: String): List<Posicion> {
    val puntos = mutableListOf<Posicion>()
    var indice = 0
    var lat = 0
    var lng = 0
    while (indice < codigo.length) {
        // Cada coordenada viene como una diferencia respecto a la anterior
        var resultado = 0
        var desplazamiento = 0
        var b: Int
        do {
            b = codigo[indice++].code - 63
            resultado = resultado or ((b and 0x1f) shl desplazamiento)
            desplazamiento += 5
        } while (b >= 0x20)
        lat += if (resultado and 1 != 0) (resultado shr 1).inv() else resultado shr 1

        resultado = 0
        desplazamiento = 0
        do {
            b = codigo[indice++].code - 63
            resultado = resultado or ((b and 0x1f) shl desplazamiento)
            desplazamiento += 5
        } while (b >= 0x20)
        lng += if (resultado and 1 != 0) (resultado shr 1).inv() else resultado shr 1

        puntos.add(Posicion(lat / 1e5, lng / 1e5))
    }
    return puntos
}
