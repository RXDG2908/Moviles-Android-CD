package com.tecsup.huellitasvet.model

import java.time.LocalTime

// Genera los 18 horarios del día: de 09:00 a 17:30 cada 30 minutos
object Horarios {
    private val INICIO: LocalTime = LocalTime.of(9, 0)
    private const val CANTIDAD = 18
    private const val INTERVALO_MIN = 30L

    val delDia: List<String> = (0 until CANTIDAD).map { i ->
        INICIO.plusMinutes(i * INTERVALO_MIN).toString() // "09:00", "09:30", ...
    }
}
