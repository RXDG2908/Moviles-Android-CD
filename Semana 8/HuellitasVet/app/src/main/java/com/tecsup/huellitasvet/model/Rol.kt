package com.tecsup.huellitasvet.model

// Perfil del usuario. Se guarda en Room y en DataStore como Rol.name
// ADMIN es el veterinario (nombre heredado del proyecto base); ADMINISTRADOR controla toda la app.
enum class Rol(val etiqueta: String) {
    CLIENTE("Dueño"),
    ADMIN("Veterinario"),
    ADMINISTRADOR("Admin")
}
