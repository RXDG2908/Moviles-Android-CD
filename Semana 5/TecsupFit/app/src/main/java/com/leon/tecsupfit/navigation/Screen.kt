package com.leon.tecsupfit.navigation

// Clase sellada con todas las rutas de la app
sealed class Screen(val route: String) {

    // Pantallas de la bottomBar
    object Home     : Screen("home")
    object Reservas : Screen("reservas")
    object Rutinas  : Screen("rutinas")
    object Perfil   : Screen("perfil")

    // RUTA CON ARGUMENTO: id de la clase elegida en Inicio
    object Detalle : Screen("detalle/{claseId}") {
        fun createRoute(claseId: Int): String = "detalle/$claseId"
    }

    // RUTA CON DOS ARGUMENTOS: la clase y la posición del horario elegido
    object Confirmacion : Screen("confirmacion/{claseId}/{horarioIndex}") {
        fun createRoute(claseId: Int, horarioIndex: Int): String =
            "confirmacion/$claseId/$horarioIndex"
    }
}
