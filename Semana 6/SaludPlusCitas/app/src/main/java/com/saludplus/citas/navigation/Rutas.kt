package com.saludplus.citas.navigation

// Todas las rutas de la app. Las que llevan {parametro} reciben un dato
// de la pantalla anterior; para armarlas se usan las funciones de abajo.
object Rutas {
    // Autenticación
    const val SPLASH = "splash"
    const val REGISTRO = "registro"
    // Login recibe el teléfono opcional (lo manda Registro para dejarlo escrito)
    const val LOGIN = "login?telefono={telefono}"
    const val TERMINOS = "terminos"

    // Inicio y barra inferior (NavigationBar)
    const val HOME = "home"
    const val MIS_CITAS = "mis_citas"
    const val RESULTADOS = "resultados"
    const val PERFIL = "perfil"
    const val NOTIFICACIONES = "notificaciones"

    // Flujo de agendamiento
    // Sedes: "destino" dice a qué pantalla ir después de elegir la sede
    // e "id" lleva el dato que esa pantalla necesita (especialidadId o medicoId)
    const val SEDES = "sedes?destino={destino}&id={id}"
    // Posibles destinos después de elegir la sede
    const val IR_A_ESPECIALIDADES = "especialidades"
    const val IR_A_MEDICOS = "medicos"
    const val ESPECIALIDADES = "especialidades"
    const val MEDICOS = "medicos/{especialidadId}"
    const val FECHA_HORA = "fecha_hora/{medicoId}"
    const val CONFIRMAR_CITA = "confirmar_cita/{medicoId}/{fecha}/{hora}"
    const val CITA_EXITOSA = "cita_exitosa/{citaId}"

    // Detalle de una cita (reto extra)
    const val DETALLE_CITA = "detalle_cita/{citaId}"

    fun login(telefono: String = "") = "login?telefono=$telefono"
    fun sedes(destino: String = IR_A_ESPECIALIDADES, id: Int = 0) = "sedes?destino=$destino&id=$id"
    fun medicos(especialidadId: Int) = "medicos/$especialidadId"
    fun fechaHora(medicoId: Int) = "fecha_hora/$medicoId"
    fun confirmarCita(medicoId: Int, fecha: String, hora: String) =
        "confirmar_cita/$medicoId/$fecha/$hora"
    fun citaExitosa(citaId: Int) = "cita_exitosa/$citaId"
    fun detalleCita(citaId: Int) = "detalle_cita/$citaId"
}
