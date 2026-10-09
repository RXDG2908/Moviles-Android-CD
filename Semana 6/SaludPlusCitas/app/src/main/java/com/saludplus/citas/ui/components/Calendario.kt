package com.saludplus.citas.ui.components

import java.time.DayOfWeek
import java.time.LocalDate

// Funciones de fecha para el calendario de la Pantalla 6 y la fecha de la Pantalla 7.
// Los nombres se escriben a mano para que salgan en español de Perú ("Setiembre")
// sin depender del idioma del celular.

private val diasCortos = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
private val diasLargos = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
private val meses = listOf(
    "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
    "Julio", "Agosto", "Setiembre", "Octubre", "Noviembre", "Diciembre"
)

// Los primeros "cantidad" días hábiles desde "desde" (incluido), sin sábados ni domingos
fun diasHabiles(desde: LocalDate, cantidad: Int = 5): List<LocalDate> {
    val resultado = mutableListOf<LocalDate>()
    var dia = desde
    while (resultado.size < cantidad) {
        if (dia.dayOfWeek != DayOfWeek.SATURDAY && dia.dayOfWeek != DayOfWeek.SUNDAY) {
            resultado.add(dia)
        }
        dia = dia.plusDays(1)
    }
    return resultado
}

// LocalDate -> "Mar" (dayOfWeek.value va de 1 = lunes a 7 = domingo)
fun nombreDiaCorto(fecha: LocalDate): String = diasCortos[fecha.dayOfWeek.value - 1]

// LocalDate -> "Octubre 2026"
fun mesYAnio(fecha: LocalDate): String = "${meses[fecha.monthValue - 1]} ${fecha.year}"

// "2026-09-16" -> "Miércoles 16 de setiembre 2026"
fun fechaLarga(fecha: String): String {
    val dia = LocalDate.parse(fecha)
    val nombreDia = diasLargos[dia.dayOfWeek.value - 1]
    val mes = meses[dia.monthValue - 1].lowercase()
    return "$nombreDia ${dia.dayOfMonth} de $mes ${dia.year}"
}
