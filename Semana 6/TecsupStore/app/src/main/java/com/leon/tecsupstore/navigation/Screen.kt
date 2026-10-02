package com.leon.tecsupstore.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Detalle : Screen("detalle/{productoId}") {
        fun createRoute(productoId: Int) = "detalle/$productoId"
    }
}
