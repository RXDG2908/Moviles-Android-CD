package com.tecsup.huellitasvet.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale

// Ayudas con java.time para las reglas con fechas y horas
object Fechas {
    private val ES = Locale("es", "PE")

    // "2026-10-21" + "10:30" -> LocalDateTime
    fun fechaHora(fecha: String, hora: String): LocalDateTime =
        LocalDateTime.of(LocalDate.parse(fecha), LocalTime.parse(hora))

    // Edad aproximada: año actual - año de nacimiento
    fun edad(anioNacimiento: Int): Int = LocalDate.now().year - anioNacimiento

    fun textoEdad(anioNacimiento: Int): String {
        val e = edad(anioNacimiento)
        return if (e == 1) "1 año" else "$e años"
    }

    // ---------- Textos cortos para la UI ----------
    // "Mié"
    fun diaSemana(fecha: LocalDate): String =
        fecha.dayOfWeek.getDisplayName(TextStyle.SHORT, ES).replace(".", "").replaceFirstChar { it.uppercase() }

    // "oct"
    private fun mes(fecha: LocalDate): String =
        fecha.month.getDisplayName(TextStyle.SHORT, ES).replace(".", "").lowercase()

    // "12 oct"
    fun diaMes(fecha: String): String {
        val f = LocalDate.parse(fecha)
        return "${f.dayOfMonth} ${mes(f)}"
    }

    // "Mié 21 oct"
    fun larga(fecha: String): String {
        val f = LocalDate.parse(fecha)
        return "${diaSemana(f)} ${f.dayOfMonth} ${mes(f)}"
    }

    // "Mié 21"
    fun corta(fecha: String): String {
        val f = LocalDate.parse(fecha)
        return "${diaSemana(f)} ${f.dayOfMonth}"
    }
}
