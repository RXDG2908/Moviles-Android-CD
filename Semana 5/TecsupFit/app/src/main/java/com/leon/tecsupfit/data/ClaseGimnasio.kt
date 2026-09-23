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
    // Una clase no se puede crear sin nombre ni sin horarios
    init {
        require(nombre.isNotBlank()) { "La clase debe tener nombre" }
        require(horarios.isNotEmpty()) { "La clase debe tener al menos un horario" }
    }

    // Tipo de clase (lo decide cada clase concreta)
    abstract val tipo: String

    // Un dato propio de cada tipo de clase
    abstract fun detalleExtra(): String

    // Recomendación general; cada tipo de clase puede cambiarla
    open fun recomendacion(): String = "Llega 10 minutos antes de la clase"
}

// Clases de cardio: suben el ritmo cardíaco y queman calorías
class ClaseCardio(
    id: Int,
    nombre: String,
    dia: String,
    horario: String,
    sala: String,
    duracion: String,
    descripcion: String,
    cupos: String,
    horarios: List<String>,
    val calorias: Int          // calorías aproximadas que se queman en la clase
) : ClaseGimnasio(id, nombre, dia, horario, sala, duracion, descripcion, cupos, horarios) {

    override val tipo: String = "Cardio"

    override fun detalleExtra(): String = "Quema aproximada: $calorias kcal"

    override fun recomendacion(): String = "Trae agua y una toalla"
}

// Clases de bienestar: flexibilidad, postura y respiración
class ClaseBienestar(
    id: Int,
    nombre: String,
    dia: String,
    horario: String,
    sala: String,
    duracion: String,
    descripcion: String,
    cupos: String,
    horarios: List<String>,
    val nivel: String          // nivel recomendado: Principiante, Intermedio...
) : ClaseGimnasio(id, nombre, dia, horario, sala, duracion, descripcion, cupos, horarios) {

    override val tipo: String = "Bienestar"

    override fun detalleExtra(): String = "Nivel recomendado: $nivel"

    override fun recomendacion(): String = "Usa ropa cómoda y trae tu mat"
}
