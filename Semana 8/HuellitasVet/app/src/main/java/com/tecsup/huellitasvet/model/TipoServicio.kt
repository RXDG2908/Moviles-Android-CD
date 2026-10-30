package com.tecsup.huellitasvet.model

// Mejora: tipo de servicio. Se guarda en Room como TipoServicio.name
enum class TipoServicio(val etiqueta: String, val descripcion: String) {
    SALUD("Salud", "Consultas y chequeos"),
    ESTETICA("Estética", "Baños y cortes"),
    PREVENCION("Prevención", "Vacunas y cuidados");

    companion object {
        fun de(nombre: String): TipoServicio = entries.firstOrNull { it.name == nombre } ?: SALUD
    }
}
