package com.tecsup.huellitasvet.model

// Perfil del usuario. Se guarda en Room y en DataStore como Rol.name
// ADMIN es el veterinario (nombre heredado del proyecto base); ADMINISTRADOR controla toda la app.
enum class Rol(val etiqueta: String) {
    CLIENTE("Dueño"),
    ADMIN("Veterinario"),
    // Mejora (administrador): tercer rol con control total. Al iniciar sesión va a su grafo
    // (NavVet.irAlInicio), usa su tema terracota (VetTheme) y sus pantallas están en ui/admin.
    ADMINISTRADOR("Admin")
}
