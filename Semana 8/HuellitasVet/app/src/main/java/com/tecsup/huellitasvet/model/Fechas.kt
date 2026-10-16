package com.tecsup.huellitasvet.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

// Ayudas con java.time para las reglas con fechas y horas
object Fechas {
    // "2026-10-21" + "10:30" -> LocalDateTime
    fun fechaHora(fecha: String, hora: String): LocalDateTime =
        LocalDateTime.of(LocalDate.parse(fecha), LocalTime.parse(hora))

    // Edad aproximada: año actual - año de nacimiento
    fun edad(anioNacimiento: Int): Int = LocalDate.now().year - anioNacimiento

    fun textoEdad(anioNacimiento: Int): String {
        val e = edad(anioNacimiento)
        return if (e == 1) "1 año" else "$e años"
    }
}
