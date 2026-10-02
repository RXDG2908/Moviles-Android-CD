package com.leon.tecsupstore.navigation

// Clase sellada con todas las rutas de la app
sealed class Screen(val route: String) {

    // Destinos del menú lateral (drawer)
    object Home      : Screen("home")
    object Pedidos   : Screen("pedidos")
    object Favoritos : Screen("favoritos")
    object Perfil    : Screen("perfil")

    // RUTA CON ARGUMENTO: id del producto elegido en Inicio
    object Detalle : Screen("detalle/{productoId}") {
        fun createRoute(productoId: Int): String = "detalle/$productoId"
    }
}
