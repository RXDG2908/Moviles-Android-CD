package com.leon.clinicasalud.navigation

// Clase sellada con todas las rutas de la app
sealed class Screen(val route: String) {

    // Pantallas del menú lateral (drawer)
    object Home      : Screen("home")
    object Citas     : Screen("citas")
    object Historial : Screen("historial")
    object Perfil    : Screen("perfil")

    // RUTA CON ARGUMENTO: id del médico elegido en Inicio
    object Medico : Screen("medico/{medicoId}") {
        fun createRoute(medicoId: Int): String = "medico/$medicoId"
    }

    // El mismo médico pasa a la pantalla de agendar
    object Agendar : Screen("agendar/{medicoId}") {
        fun createRoute(medicoId: Int): String = "agendar/$medicoId"
    }

    // RUTA CON TRES ARGUMENTOS: médico, fecha y hora elegidas
    object Confirmacion : Screen("confirmacion/{medicoId}/{fechaIndex}/{horaIndex}") {
        fun createRoute(medicoId: Int, fechaIndex: Int, horaIndex: Int): String =
            "confirmacion/$medicoId/$fechaIndex/$horaIndex"
    }
}
