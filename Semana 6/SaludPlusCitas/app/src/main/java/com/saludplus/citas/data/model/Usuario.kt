package com.saludplus.citas.data.model

// Paciente registrado en la app (campos del formulario de Registro).
// El teléfono identifica al usuario; el correo es opcional.
data class Usuario(
    val nombre: String,
    val telefono: String,
    val correo: String,
    val contrasena: String
)
