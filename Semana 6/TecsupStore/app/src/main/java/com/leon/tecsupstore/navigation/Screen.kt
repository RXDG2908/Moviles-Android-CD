package com.leon.tecsupstore.navigation

// Clase sellada con todas las rutas de la app
sealed class Screen(val route: String) {

    object Home : Screen("home")

    // RUTA CON ARGUMENTO: id del producto elegido en Inicio
    object Detalle : Screen("detalle/{productoId}") {
        fun createRoute(productoId: Int): String = "detalle/$productoId"
    }
}
