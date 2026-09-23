package com.leon.tecsupfit.data

// Contrato de toda clase del gimnasio: qué datos tiene y qué sabe decir de sí misma.
// Es abstracta: no se puede crear una "ClaseGimnasio" directamente.
abstract class ClaseGimnasio(
    val id: Int,
    val nombre: String,
    val dia: String,          // "Hoy" o "Esta semana" (sirve para el filtro)
    val horario: String,
    val sala: String,
    val duracion: String,
    val descripcion: String,
    val cupos: String,
    val horarios: List<String> // opciones para elegir antes de reservar
) {
    // Tipo de clase (lo decide cada clase concreta)
    abstract val tipo: String

    // Un dato propio de cada tipo de clase
    abstract fun detalleExtra(): String
}

// Clase concreta para las clases grupales del gimnasio
class ClaseGrupal(
    id: Int,
    nombre: String,
    dia: String,
    horario: String,
    sala: String,
    duracion: String,
    descripcion: String,
    cupos: String,
    horarios: List<String>
) : ClaseGimnasio(id, nombre, dia, horario, sala, duracion, descripcion, cupos, horarios) {

    override val tipo: String = "Grupal"

    override fun detalleExtra(): String = "Clase grupal con instructor"
}
