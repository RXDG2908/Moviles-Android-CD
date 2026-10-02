package com.leon.tecsupstore.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.leon.tecsupstore.screens.AppDrawer
import com.leon.tecsupstore.screens.DetalleProductoScreen
import com.leon.tecsupstore.screens.HomeScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    // Estado del menú lateral y scope para abrirlo/cerrarlo (son animaciones)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // El drawer ENVUELVE a toda la app: queda encima de cualquier pantalla y su topBar
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(onCerrar = { scope.launch { drawerState.close() } })
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navController = navController,
                    onAbrirMenu = { scope.launch { drawerState.open() } }
                )
            }
            composable(
                route = Screen.Detalle.route,
                arguments = listOf(
                    navArgument("productoId") {
                        type = NavType.IntType
                        defaultValue = 1
                    }
                )
            ) { backStackEntry ->
                val productoId = backStackEntry.arguments?.getInt("productoId") ?: 1
                DetalleProductoScreen(navController, productoId)
            }
        }
    }
}
