package com.saludplus.citas.data.model

// Cita agendada por un paciente.
// fecha: texto con formato "yyyy-MM-dd" (ej. "2026-09-16").
// hora: texto con formato "HH:mm" (ej. "09:30").
data class Cita(
    val id: Int,
    val telefonoUsuario: String,
    val medicoId: Int,
    val fecha: String,
    val hora: String,
    val motivo: String = "",
    val estado: String = "Confirmada",
    val sedeId: Int = 0         // sede donde se atenderá la cita
)
