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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
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
import androidx.navigation.compose.dialog
import androidx.compose.ui.window.DialogProperties
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
    val errores by loginVm.errores.collectAsStateWithLifecycle()

    val actual = sesion
    if (revisando) {
        FondoCielo { Cargando() }
    } else if (actual == null) {
        LoginScreen(
            estado = login,
            errores = errores,
            onIngresar = { c, k -> loginVm.ingresar(c, k) },
            onEditarCorreo = { loginVm.alEditarCorreo() },
            onEditarClave = { loginVm.alEditarClave() }
        )
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
    val rutaActual = entrada?.destination?.route ?: RUTA_LISTA
    val ruta = if (rutaActual.startsWith(RUTA_DETALLE)) nav.previousBackStackEntry?.destination?.route ?: RUTA_LISTA else rutaActual
    val enDetalle = ruta.startsWith(RUTA_DETALLE)

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text(tituloDe(ruta), style = MaterialTheme.typography.headlineMedium) },
                navigationIcon = {
                    if (enDetalle) {
                        IconButton(onClick = { nav.popBackStack() }) {
                            BotonRedondo { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver") }
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onSalir) {
                        BotonRedondo { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Cerrar sesión") }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = {
            if (!enDetalle) {
                // Barra inferior como .tabbar de la web: la activa en amarillo con raya arriba
                NavigationBar(containerColor = Noche) {
                    NavigationBarItem(
                        selected = ruta == RUTA_LISTA,
                        onClick = { irA(nav, RUTA_LISTA) },
                        icon = { IconoTab(ruta == RUTA_LISTA) { Icon(Icons.Filled.ShoppingBag, contentDescription = null) } },
                        label = { Text("Tienda", style = MaterialTheme.typography.labelLarge) },
                        colors = coloresBarra()
                    )
                    NavigationBarItem(
                        selected = ruta == RUTA_FAVORITOS,
                        onClick = { irA(nav, RUTA_FAVORITOS) },
                        icon = { IconoTab(ruta == RUTA_FAVORITOS) { Icon(Icons.Filled.Star, contentDescription = null) } },
                        label = { Text("Favoritos", style = MaterialTheme.typography.labelLarge) },
                        colors = coloresBarra()
                    )
                    // El panel solo aparece para el administrador
                    if (sesion.rol == Rol.ADMIN) {
                        NavigationBarItem(
                            selected = ruta == RUTA_ADMIN,
                            onClick = { irA(nav, RUTA_ADMIN) },
                            icon = { IconoTab(ruta == RUTA_ADMIN) { Icon(Icons.Filled.AdminPanelSettings, contentDescription = null) } },
                            label = { Text("Admin", style = MaterialTheme.typography.labelLarge) },
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
                val refrescando by vm.refrescando.collectAsStateWithLifecycle()
                ListaScreen(
                    estado = estado,
                    aviso = aviso,
                    onReintentar = { vm.reintentar() },
                    onItemClick = { id -> nav.navigate("$RUTA_DETALLE/$id") },
                    onFavorito = { id -> vm.alternarFavorito(id) },
                    refrescando = refrescando,
                    onActualizar = { vm.deslizarParaActualizar() }
                )
            }
            // La hoja del producto se abre encima de la tienda, con un velo detrás
            dialog(
                "$RUTA_DETALLE/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
                dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
            ) { backStack ->
                val id = backStack.arguments?.getString("id") ?: ""
                val vm: DetalleViewModel = viewModel(factory = ViewModelFactory(container, id))
                val estado by vm.estado.collectAsStateWithLifecycle()
                DetalleScreen(
                    estado = estado,
                    onReintentar = { vm.cargar() },
                    onFavorito = { vm.alternarFavorito() },
                    onCerrar = { nav.popBackStack() }
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

// Fondo oscuro y redondo para los iconos de la barra superior
@Composable
private fun BotonRedondo(icono: @Composable () -> Unit) {
    Box(
        Modifier.size(40.dp).clip(CircleShape).background(Noche.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) { icono() }
}

// Icono de la barra inferior; el activo lleva una raya amarilla arriba
@Composable
private fun IconoTab(activo: Boolean, icono: @Composable () -> Unit) {
    androidx.compose.foundation.layout.Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .size(width = 28.dp, height = 3.dp)
                .clip(CircleShape)
                .background(if (activo) Oro else Color.Transparent)
        )
        androidx.compose.foundation.layout.Spacer(Modifier.size(6.dp))
        icono()
    }
}

// Título de la barra superior según la pantalla
private fun tituloDe(ruta: String) = when {
    ruta.startsWith(RUTA_DETALLE) -> "DETALLE"
    ruta == RUTA_FAVORITOS -> "FAVORITOS"
    ruta == RUTA_ADMIN -> "PANEL ADMIN"
    else -> "TECSUPNITE"
}

@Composable
private fun coloresBarra() = NavigationBarItemDefaults.colors(
    selectedIconColor = Oro,
    selectedTextColor = Oro,
    indicatorColor = Color.Transparent,
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
