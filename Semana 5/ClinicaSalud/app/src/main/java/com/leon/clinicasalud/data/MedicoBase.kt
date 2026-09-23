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

    // Minutos que dura la consulta; cada tipo de médico puede cambiarlo
    open fun duracionConsulta(): Int = 20
}

// Especialista: atiende a adultos y pide algo para la consulta
class MedicoEspecialista(
    id: Int,
    nombre: String,
    especialidad: String,
    calificacion: Double,
    resenas: Int,
    experiencia: String,
    descripcion: String,
    val requisito: String         // lo que el paciente debe traer o hacer
) : MedicoBase(id, nombre, especialidad, calificacion, resenas, experiencia, descripcion) {

    override val tipoAtencion: String = "Especialista"

    override fun indicaciones(): String = "Para tu consulta: $requisito"

    override fun duracionConsulta(): Int = 30
}

// Pediatra: atiende a niños y adolescentes hasta cierta edad
class MedicoPediatra(
    id: Int,
    nombre: String,
    especialidad: String,
    calificacion: Double,
    resenas: Int,
    experiencia: String,
    descripcion: String,
    val edadMaxima: Int           // edad máxima de los pacientes que atiende
) : MedicoBase(id, nombre, especialidad, calificacion, resenas, experiencia, descripcion) {

    override val tipoAtencion: String = "Pediatría"

    override fun indicaciones(): String = "Atiende pacientes de hasta $edadMaxima años"
}
