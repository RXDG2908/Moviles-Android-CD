package com.leon.tecsupfit.data

import androidx.compose.runtime.mutableStateListOf

// Objeto que guarda las reservas del usuario y sabe operar con ellas
class GestorReservas {

    // Lista interna y observable: solo el gestor puede agregar o quitar
    private val lista = mutableStateListOf(
        Reserva("Yoga funcional", "Ayer, 7:00 am", "Completada")
    )

    // Hacia afuera la lista se entrega solo para leer
    val reservas: List<Reserva>
        get() = lista

    // Crea una reserva confirmada. Devuelve false si ya existía la misma reserva
    fun reservar(clase: ClaseGimnasio, horario: String): Boolean {
        require(horario in clase.horarios) { "El horario no pertenece a la clase" }
        val nueva = Reserva(clase.nombre, "Hoy, $horario", "Confirmada")
        for (r in lista) {
            if (r == nueva) {
                return false
            }
        }
        lista.add(nueva)
        return true
    }

    // Solo se cancelan las reservas que todavía están confirmadas
    fun cancelar(reserva: Reserva) {
        if (reserva.estado == "Confirmada") {
            lista.remove(reserva)
        }
    }

    // Cuántas reservas siguen confirmadas
    fun reservasActivas(): Int {
        var total = 0
        for (r in lista) {
            if (r.estado == "Confirmada") {
                total++
            }
        }
        return total
    }

    fun hayConfirmadas(): Boolean {
        return reservasActivas() > 0
    }
}
