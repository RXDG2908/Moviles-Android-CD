package com.tecsup.huellitasvet.model

// Perfil del usuario. Se guarda en Room y en DataStore como Rol.name
enum class Rol(val etiqueta: String) {
    CLIENTE("Dueño"),
    ADMIN("Veterinario")
}
