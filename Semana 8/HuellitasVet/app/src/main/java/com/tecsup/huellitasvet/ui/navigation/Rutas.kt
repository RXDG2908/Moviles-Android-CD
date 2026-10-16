package com.tecsup.huellitasvet.ui.navigation

// [E] Rutas de la app
object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"

    // Dueño
    const val INICIO = "inicio"
    const val MASCOTAS = "mascotas"
    const val FORMULARIO_MASCOTA = "formulario_mascota?id={id}"
    const val FICHA_MASCOTA = "ficha_mascota/{id}"
    const val PEDIR_CITA = "pedir_cita?mascotaId={mascotaId}"
    const val MIS_CITAS = "mis_citas"
    const val PERFIL = "perfil"

    // Veterinario
    const val PANEL = "panel"
    const val AGENDA = "agenda"
    const val ATENDER_CITA = "atender_cita/{id}"
    const val PACIENTES = "pacientes"
    const val SERVICIOS = "servicios"
    const val FORMULARIO_SERVICIO = "formulario_servicio?id={id}"
    const val AJUSTES = "ajustes"

    fun formularioMascota(id: Int? = null) = if (id == null) "formulario_mascota" else "formulario_mascota?id=$id"
    fun fichaMascota(id: Int) = "ficha_mascota/$id"
    fun pedirCita(mascotaId: Int? = null) = if (mascotaId == null) "pedir_cita" else "pedir_cita?mascotaId=$mascotaId"
    fun atenderCita(id: Int) = "atender_cita/$id"
    fun formularioServicio(id: Int? = null) = if (id == null) "formulario_servicio" else "formulario_servicio?id=$id"
}
