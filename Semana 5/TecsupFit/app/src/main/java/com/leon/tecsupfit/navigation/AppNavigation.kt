package com.leon.tecsupfit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.leon.tecsupfit.screens.ConfirmacionScreen
import com.leon.tecsupfit.screens.DetalleScreen
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
        composable(
            route = Screen.Detalle.route,
            arguments = listOf(
                navArgument("claseId") {
                    type = NavType.IntType
                    defaultValue = 1
                }
            )
        ) { backStackEntry ->
            val claseId = backStackEntry.arguments?.getInt("claseId") ?: 1
            DetalleScreen(navController, claseId)
        }
        composable(
            route = Screen.Confirmacion.route,
            arguments = listOf(
                navArgument("claseId") {
                    type = NavType.IntType
                    defaultValue = 1
                },
                navArgument("horarioIndex") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val claseId = backStackEntry.arguments?.getInt("claseId") ?: 1
            val horarioIndex = backStackEntry.arguments?.getInt("horarioIndex") ?: 0
            ConfirmacionScreen(navController, claseId, horarioIndex)
        }
    }
}
