package com.leon.clinicasalud.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.leon.clinicasalud.screens.CitasScreen
import com.leon.clinicasalud.screens.HistorialScreen
import com.leon.clinicasalud.screens.HomeScreen
import com.leon.clinicasalud.screens.PerfilScreen

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
        composable(Screen.Citas.route) {
            CitasScreen(navController)
        }
        composable(Screen.Historial.route) {
            HistorialScreen(navController)
        }
        composable(Screen.Perfil.route) {
            PerfilScreen(navController)
        }
    }
}
