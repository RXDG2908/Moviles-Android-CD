package com.leon.clinicasalud.data

import androidx.compose.runtime.mutableStateListOf

// Objeto que guarda las citas del paciente y sabe operar con ellas
class Agenda {

    // Lista observable: al agregar o quitar, las pantallas se redibujan
    val citas = mutableStateListOf(
        Cita("Dr. Luis Vega", "Miércoles 15", "3:00 pm", "Completada")
    )

    // Crea una cita confirmada con el médico, la fecha y la hora elegidos
    fun agendar(medico: MedicoBase, fecha: String, hora: String) {
        citas.add(Cita(medico.nombre, fecha, hora, "Confirmada"))
    }

    fun cancelar(cita: Cita) {
        citas.remove(cita)
    }
}
