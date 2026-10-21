package com.tecsup.huellitasvet.model

// Estados de una cita. ATENDIDA y CANCELADA son estados finales
enum class EstadoCita(val etiqueta: String) {
    PENDIENTE("Pendiente"),
    CONFIRMADA("Confirmada"),
    ATENDIDA("Atendida"),
    CANCELADA("Cancelada")
}
