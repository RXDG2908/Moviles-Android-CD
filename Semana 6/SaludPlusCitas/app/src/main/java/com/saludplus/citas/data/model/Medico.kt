package com.saludplus.citas.data.model

// Médico de la clínica. Pertenece a una especialidad (especialidadId).
data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val cargo: String,          // ej. "Ginecóloga"
    val cmp: String,            // colegiatura, ej. "12345"
    val calificacion: Double,   // ej. 4.9
    val resenas: Int,           // ej. 120
    val disponibilidad: String, // ej. "Disponible hoy"
    val sedes: List<Int> = listOf(1, 2), // ids de las sedes donde atiende
    // Días de la semana en que atiende: 1 = lunes ... 5 = viernes
    val diasAtencion: List<Int> = listOf(1, 2, 3, 4, 5)
)
