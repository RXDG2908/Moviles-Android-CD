package com.leon.clinicasalud.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.leon.clinicasalud.screens.AgendarScreen
import com.leon.clinicasalud.screens.CitasScreen
import com.leon.clinicasalud.screens.ConfirmacionScreen
import com.leon.clinicasalud.screens.HistorialScreen
import com.leon.clinicasalud.screens.HomeScreen
import com.leon.clinicasalud.screens.MedicoScreen
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
        composable(
            route = Screen.Medico.route,
            arguments = listOf(
                navArgument("medicoId") {
                    type = NavType.IntType
                    defaultValue = 1
                }
            )
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 1
            MedicoScreen(navController, medicoId)
        }
        composable(
            route = Screen.Agendar.route,
            arguments = listOf(
                navArgument("medicoId") {
                    type = NavType.IntType
                    defaultValue = 1
                }
            )
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 1
            AgendarScreen(navController, medicoId)
        }
        composable(
            route = Screen.Confirmacion.route,
            arguments = listOf(
                navArgument("medicoId") {
                    type = NavType.IntType
                    defaultValue = 1
                },
                navArgument("fechaIndex") {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument("horaIndex") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 1
            val fechaIndex = backStackEntry.arguments?.getInt("fechaIndex") ?: 0
            val horaIndex = backStackEntry.arguments?.getInt("horaIndex") ?: 0
            ConfirmacionScreen(navController, medicoId, fechaIndex, horaIndex)
        }
    }
}
