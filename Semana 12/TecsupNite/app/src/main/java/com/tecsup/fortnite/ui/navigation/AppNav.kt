package com.tecsup.fortnite.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color
import com.tecsup.fortnite.ui.theme.Blanco
import com.tecsup.fortnite.ui.theme.FondoCielo
import com.tecsup.fortnite.ui.theme.Noche
import com.tecsup.fortnite.ui.theme.Oro
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.fortnite.AppContainer
import com.tecsup.fortnite.domain.model.Rol
import com.tecsup.fortnite.domain.model.Sesion
import com.tecsup.fortnite.ui.ViewModelFactory
import com.tecsup.fortnite.ui.admin.AdminScreen
import com.tecsup.fortnite.ui.admin.AdminViewModel
import com.tecsup.fortnite.ui.components.Cargando
import com.tecsup.fortnite.ui.detalle.DetalleScreen
import com.tecsup.fortnite.ui.detalle.DetalleViewModel
import com.tecsup.fortnite.ui.favoritos.FavoritosScreen
import com.tecsup.fortnite.ui.favoritos.FavoritosViewModel
import com.tecsup.fortnite.ui.lista.ListaScreen
import com.tecsup.fortnite.ui.lista.ListaViewModel
import com.tecsup.fortnite.ui.login.LoginScreen
import com.tecsup.fortnite.ui.login.LoginViewModel

const val RUTA_LISTA = "lista"
const val RUTA_FAVORITOS = "favoritos"
const val RUTA_ADMIN = "admin"
const val RUTA_DETALLE = "detalle"

@Composable
fun AppNav(container: AppContainer) {
    val loginVm: LoginViewModel = viewModel(factory = ViewModelFactory(container))
    val sesion by loginVm.sesion.collectAsStateWithLifecycle()
    val revisando by loginVm.revisando.collectAsStateWithLifecycle()
    val login by loginVm.login.collectAsStateWithLifecycle()

    val actual = sesion
    if (revisando) {
        Cargando()
    } else if (actual == null) {
        LoginScreen(estado = login, onIngresar = { c, k -> loginVm.ingresar(c, k) })
    } else {
        FondoCielo {
            Principal(container, actual, onSalir = { loginVm.cerrarSesion() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Principal(container: AppContainer, sesion: Sesion, onSalir: () -> Unit) {
    val nav = rememberNavController()
    val entrada by nav.currentBackStackEntryAsState()
    val ruta = entrada?.destination?.route ?: RUTA_LISTA
    val enDetalle = ruta.startsWith(RUTA_DETALLE)

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text(tituloDe(ruta)) },
                navigationIcon = {
                    if (enDetalle) {
                        IconButton(onClick = { nav.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onSalir) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            if (!enDetalle) {
                NavigationBar {
                    NavigationBarItem(
                        selected = ruta == RUTA_LISTA,
                        onClick = { irA(nav, RUTA_LISTA) },
                        icon = { Icon(Icons.Filled.ShoppingBag, contentDescription = null) },
                        label = { Text("TIENDA", style = MaterialTheme.typography.labelLarge) },
                        colors = coloresBarra()
                    )
                    NavigationBarItem(
                        selected = ruta == RUTA_FAVORITOS,
                        onClick = { irA(nav, RUTA_FAVORITOS) },
                        icon = { Icon(Icons.Filled.Star, contentDescription = null) },
                        label = { Text("FAVORITOS", style = MaterialTheme.typography.labelLarge) },
                        colors = coloresBarra()
                    )
                    // El panel solo aparece para el administrador
                    if (sesion.rol == Rol.ADMIN) {
                        NavigationBarItem(
                            selected = ruta == RUTA_ADMIN,
                            onClick = { irA(nav, RUTA_ADMIN) },
                            icon = { Icon(Icons.Filled.AdminPanelSettings, contentDescription = null) },
                            label = { Text("ADMIN", style = MaterialTheme.typography.labelLarge) },
                            colors = coloresBarra()
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(nav, startDestination = RUTA_LISTA, modifier = Modifier.padding(padding)) {
            composable(RUTA_LISTA) {
                val vm: ListaViewModel = viewModel(factory = ViewModelFactory(container))
                val estado by vm.estado.collectAsStateWithLifecycle()
                val aviso by vm.aviso.collectAsStateWithLifecycle()
                ListaScreen(
                    estado = estado,
                    aviso = aviso,
                    onReintentar = { vm.reintentar() },
                    onItemClick = { id -> nav.navigate("$RUTA_DETALLE/$id") },
                    onFavorito = { id -> vm.alternarFavorito(id) }
                )
            }
            composable(
                "$RUTA_DETALLE/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStack ->
                val id = backStack.arguments?.getString("id") ?: ""
                val vm: DetalleViewModel = viewModel(factory = ViewModelFactory(container, id))
                val estado by vm.estado.collectAsStateWithLifecycle()
                DetalleScreen(
                    estado = estado,
                    onReintentar = { vm.cargar() },
                    onFavorito = { vm.alternarFavorito() }
                )
            }
            composable(RUTA_FAVORITOS) {
                val vm: FavoritosViewModel = viewModel(factory = ViewModelFactory(container))
                val estado by vm.estado.collectAsStateWithLifecycle()
                FavoritosScreen(
                    estado = estado,
                    onItemClick = { id -> nav.navigate("$RUTA_DETALLE/$id") },
                    onFavorito = { id -> vm.alternarFavorito(id) }
                )
            }
            composable(RUTA_ADMIN) {
                // Aunque alguien llegue aquí sin ser admin, no ve nada
                if (sesion.rol == Rol.ADMIN) {
                    val vm: AdminViewModel = viewModel(factory = ViewModelFactory(container))
                    val estado by vm.estado.collectAsStateWithLifecycle()
                    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
                    AdminScreen(
                        estado = estado,
                        mensaje = mensaje,
                        onSincronizar = { vm.sincronizar() },
                        onLimpiar = { vm.limpiarCache() }
                    )
                }
            }
        }
    }
}

// Título de la barra superior según la pantalla
private fun tituloDe(ruta: String) = when {
    ruta.startsWith(RUTA_DETALLE) -> "DETALLE"
    ruta == RUTA_FAVORITOS -> "FAVORITOS"
    ruta == RUTA_ADMIN -> "PANEL ADMIN"
    else -> "TIENDA DE HOY"
}

@Composable
private fun coloresBarra() = NavigationBarItemDefaults.colors(
    selectedIconColor = Noche,
    selectedTextColor = Oro,
    indicatorColor = Oro,
    unselectedIconColor = Blanco,
    unselectedTextColor = Blanco
)

private fun irA(nav: androidx.navigation.NavController, ruta: String) {
    nav.navigate(ruta) {
        popUpTo(RUTA_LISTA) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
