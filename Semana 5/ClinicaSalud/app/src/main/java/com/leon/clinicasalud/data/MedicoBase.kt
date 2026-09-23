package com.leon.clinicasalud.data

// Contrato de todo médico de la clínica: qué datos tiene y qué sabe decir de sí mismo.
// Es abstracta: no se puede crear un "MedicoBase" directamente.
abstract class MedicoBase(
    val id: Int,
    val nombre: String,
    val especialidad: String,     // sirve para el filtro de chips
    val calificacion: Double,
    val resenas: Int,
    val experiencia: String,
    val descripcion: String
) {
    // Tipo de atención (lo decide cada médico concreto)
    abstract val tipoAtencion: String

    // Indicaciones que el paciente debe saber antes de la consulta
    abstract fun indicaciones(): String
}

// Médico concreto para los médicos actuales de la clínica
class MedicoGeneral(
    id: Int,
    nombre: String,
    especialidad: String,
    calificacion: Double,
    resenas: Int,
    experiencia: String,
    descripcion: String
) : MedicoBase(id, nombre, especialidad, calificacion, resenas, experiencia, descripcion) {

    override val tipoAtencion: String = "Consulta general"

    override fun indicaciones(): String = "Llega 15 minutos antes de tu cita"
}
