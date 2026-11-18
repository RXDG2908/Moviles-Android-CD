// Convierte una distancia en metros a un texto como "350 m" o "1.5 km".
package com.tecsup.campusgps.util

import java.util.Locale

fun formatearDistancia(metros: Float): String =
    if (metros < 1000) {
        "${metros.toInt()} m"
    } else {
        // Locale.US para que el decimal salga con punto: 1.5 km
        String.format(Locale.US, "%.1f km", metros / 1000)
    }
