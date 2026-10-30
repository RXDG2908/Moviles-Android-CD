package com.tecsup.huellitasvet.ui.navigation

// [E] Rutas de la app
object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTRO = "registro" // mejora: registro de dueños

    // Dueño
    const val INICIO = "inicio"
    const val MASCOTAS = "mascotas"
    const val FORMULARIO_MASCOTA = "formulario_mascota?id={id}"
    const val FICHA_MASCOTA = "ficha_mascota/{id}"
    const val PEDIR_CITA = "pedir_cita?mascotaId={mascotaId}&tipo={tipo}" // mejora: &tipo= (tipo de servicio ya elegido)
    const val MIS_CITAS = "mis_citas"
    const val REPROGRAMAR_CITA = "reprogramar_cita/{id}"
    const val PERFIL = "perfil"

    // Veterinario
    const val PANEL = "panel"
    const val AGENDA = "agenda"
    const val ATENDER_CITA = "atender_cita/{id}"
    const val PACIENTES = "pacientes"
    const val FICHA_PACIENTE = "ficha_paciente/{id}"
    const val SERVICIOS = "servicios"
    const val FORMULARIO_SERVICIO = "formulario_servicio?id={id}"
    const val AJUSTES = "ajustes"

    // Mejora: administrador (rutas propias porque viven en otro grafo)
    const val ADMIN_PANEL = "admin_panel"
    const val ADMIN_USUARIOS = "admin_usuarios"
    const val ADMIN_FORM_USUARIO = "admin_formulario_usuario?id={id}&rol={rol}"
    const val ADMIN_MASCOTAS = "admin_mascotas"
    const val ADMIN_FORM_MASCOTA = "admin_formulario_mascota?id={id}"
    const val ADMIN_FICHA_MASCOTA = "admin_ficha_mascota/{id}"
    const val ADMIN_SERVICIOS = "admin_servicios"
    const val ADMIN_FORM_SERVICIO = "admin_formulario_servicio?id={id}"
    const val ADMIN_AGENDA = "admin_agenda"
    const val ADMIN_ATENDER = "admin_atender/{id}"
    const val ADMIN_AJUSTES = "admin_ajustes"

    fun formularioMascota(id: Int? = null) = if (id == null) "formulario_mascota" else "formulario_mascota?id=$id"
    fun fichaMascota(id: Int) = "ficha_mascota/$id"
    fun pedirCita(mascotaId: Int? = null, tipo: String? = null): String {
        val params = listOfNotNull(mascotaId?.let { "mascotaId=$it" }, tipo?.let { "tipo=$it" })
        return if (params.isEmpty()) "pedir_cita" else "pedir_cita?" + params.joinToString("&")
    }
    fun reprogramarCita(id: Int) = "reprogramar_cita/$id"
    fun atenderCita(id: Int) = "atender_cita/$id"
    fun fichaPaciente(id: Int) = "ficha_paciente/$id"
    fun formularioServicio(id: Int? = null) = if (id == null) "formulario_servicio" else "formulario_servicio?id=$id"

    fun adminFormUsuario(id: Int? = null, rol: String? = null): String {
        val params = listOfNotNull(id?.let { "id=$it" }, rol?.let { "rol=$it" })
        return if (params.isEmpty()) "admin_formulario_usuario" else "admin_formulario_usuario?" + params.joinToString("&")
    }
    fun adminFormMascota(id: Int? = null) = if (id == null) "admin_formulario_mascota" else "admin_formulario_mascota?id=$id"
    fun adminFichaMascota(id: Int) = "admin_ficha_mascota/$id"
    fun adminFormServicio(id: Int? = null) = if (id == null) "admin_formulario_servicio" else "admin_formulario_servicio?id=$id"
    fun adminAtender(id: Int) = "admin_atender/$id"
}
