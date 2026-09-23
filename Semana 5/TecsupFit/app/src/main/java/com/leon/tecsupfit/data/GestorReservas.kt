package com.leon.tecsupfit.data

import androidx.compose.runtime.mutableStateListOf

// Objeto que guarda las reservas del usuario y sabe operar con ellas
class GestorReservas {

    // Lista observable: al agregar o quitar, las pantallas se redibujan
    val reservas = mutableStateListOf(
        Reserva("Yoga funcional", "Ayer, 7:00 am", "Completada")
    )

    // Crea una reserva confirmada para la clase y el horario elegidos
    fun reservar(clase: ClaseGimnasio, horario: String) {
        reservas.add(Reserva(clase.nombre, "Hoy, $horario", "Confirmada"))
    }

    fun cancelar(reserva: Reserva) {
        reservas.remove(reserva)
    }

    // Cuántas reservas siguen confirmadas
    fun reservasActivas(): Int {
        var total = 0
        for (r in reservas) {
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
