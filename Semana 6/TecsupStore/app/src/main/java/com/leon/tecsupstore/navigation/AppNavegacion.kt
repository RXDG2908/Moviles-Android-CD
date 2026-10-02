package com.leon.tecsupstore.navigation

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

    // Ids de los productos marcados como favoritos.
    // Vive aquí (arriba de todo) para que la usen las tarjetas y también el menú lateral.
    val favoritos = remember { mutableStateListOf<Int>() }
    // Si ya estaba lo quita, si no estaba lo agrega
    val cambiarFavorito: (Int) -> Unit = { id ->
        if (id in favoritos) favoritos.remove(id) else favoritos.add(id)
    }

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
                // El tamaño de la lista se lee aquí: si cambia, el badge se redibuja solo
                cantidadFavoritos = favoritos.size,
                onIrA = { ruta ->
                    scope.launch { drawerState.close() }
                    // Cambia de sección sin apilar pantallas: deja solo Inicio debajo
                    navController.navigate(ruta) {
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
                    }
                },
                onCerrarSesion = {
                    scope.launch { drawerState.close() }
                    // Sin login todavía: solo avisa y vuelve a Inicio limpiando el historial
                    Toast.makeText(context, "Sesion cerrada", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
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
                HomeScreen(
                    navController = navController,
                    favoritos = favoritos,
                    onCambiarFavorito = cambiarFavorito,
                    onAbrirMenu = abrirMenu
                )
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

        // Con el menú abierto, "Atrás" solo lo cierra (en vez de volver o salir).
        // Va después del NavHost para que tenga prioridad sobre su propio "Atrás".
        BackHandler(enabled = drawerState.isOpen) {
            scope.launch { drawerState.close() }
        }
    }
}
