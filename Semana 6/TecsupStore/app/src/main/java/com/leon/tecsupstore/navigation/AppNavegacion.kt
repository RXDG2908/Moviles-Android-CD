package com.leon.tecsupstore.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.leon.tecsupstore.screens.AppDrawer
import com.leon.tecsupstore.screens.DetalleProductoScreen
import com.leon.tecsupstore.screens.FavoritosScreen
import com.leon.tecsupstore.screens.HomeScreen
import com.leon.tecsupstore.screens.PedidosScreen
import com.leon.tecsupstore.screens.PerfilScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    // Estado del menú lateral y scope para abrirlo/cerrarlo (son animaciones)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Ruta de la pantalla que se ve ahora: cambia sola cada vez que se navega
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rutaActual = backStackEntry?.destination?.route

    // Las pantallas del menú; en el Detalle no se puede abrir deslizando
    val rutasDelMenu = listOf(
        Screen.Home.route, Screen.Pedidos.route, Screen.Favoritos.route, Screen.Perfil.route
    )
    val abrirMenu: () -> Unit = { scope.launch { drawerState.open() } }

    // El drawer ENVUELVE a toda la app: queda encima de cualquier pantalla y su topBar
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen || rutaActual in rutasDelMenu,
        drawerContent = {
            AppDrawer(
                rutaActual = rutaActual,
                onIrA = { ruta ->
                    scope.launch { drawerState.close() }
                    // Cambia de sección sin apilar pantallas: deja solo Inicio debajo
                    navController.navigate(ruta) {
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController = navController, onAbrirMenu = abrirMenu)
            }
            composable(Screen.Pedidos.route) {
                PedidosScreen(onAbrirMenu = abrirMenu)
            }
            composable(Screen.Favoritos.route) {
                FavoritosScreen(onAbrirMenu = abrirMenu)
            }
            composable(Screen.Perfil.route) {
                PerfilScreen(onAbrirMenu = abrirMenu)
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
