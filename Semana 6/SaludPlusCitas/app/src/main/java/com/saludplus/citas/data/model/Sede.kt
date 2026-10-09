package com.saludplus.citas.data.model

// Sede (local) de la clínica donde se atiende al paciente.
data class Sede(
    val id: Int,
    val nombre: String,
    val direccion: String,
    val distrito: String
)
