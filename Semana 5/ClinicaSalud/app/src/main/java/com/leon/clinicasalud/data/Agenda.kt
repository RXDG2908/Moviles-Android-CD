package com.leon.clinicasalud.data

import androidx.compose.runtime.mutableStateListOf

// Objeto que guarda las citas del paciente y sabe operar con ellas
class Agenda {

    // Lista interna y observable: solo la agenda puede agregar o quitar
    private val lista = mutableStateListOf(
        Cita("Dr. Luis Vega", "Miércoles 15", "3:00 pm", "Completada")
    )

    // Hacia afuera la lista se entrega solo para leer
    val citas: List<Cita>
        get() = lista

    // Crea una cita confirmada. Devuelve false si ya hay una cita a esa fecha y hora
    fun agendar(medico: MedicoBase, fecha: String, hora: String): Boolean {
        require(fecha in fechas) { "La fecha no está disponible" }
        require(hora in horas) { "La hora no está disponible" }
        for (c in lista) {
            if (c.estado == "Confirmada" && c.fecha == fecha && c.hora == hora) {
                return false
            }
        }
        lista.add(Cita(medico.nombre, fecha, hora, "Confirmada"))
        return true
    }

    // Solo se cancelan las citas que todavía están confirmadas
    fun cancelar(cita: Cita) {
        if (cita.estado == "Confirmada") {
            lista.remove(cita)
        }
    }
}
