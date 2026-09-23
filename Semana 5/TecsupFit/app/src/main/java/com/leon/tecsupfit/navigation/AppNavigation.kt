package com.leon.tecsupfit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leon.tecsupfit.screens.HomeScreen
import com.leon.tecsupfit.screens.PerfilScreen
import com.leon.tecsupfit.screens.ReservasScreen
import com.leon.tecsupfit.screens.RutinasScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Reservas.route) {
            ReservasScreen(navController)
        }
        composable(Screen.Rutinas.route) {
            RutinasScreen(navController)
        }
        composable(Screen.Perfil.route) {
            PerfilScreen(navController)
        }
    }
}
