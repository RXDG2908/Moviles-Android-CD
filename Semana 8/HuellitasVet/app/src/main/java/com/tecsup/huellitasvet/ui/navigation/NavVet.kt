package com.tecsup.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.dueno.InicioScreen
import com.tecsup.huellitasvet.ui.dueno.LoginScreen
import com.tecsup.huellitasvet.ui.dueno.MascotasScreen
import com.tecsup.huellitasvet.ui.dueno.SplashScreen
import com.tecsup.huellitasvet.ui.veterinario.ServiciosScreen
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel
import com.tecsup.huellitasvet.viewmodel.SesionViewModel
import com.tecsup.huellitasvet.viewmodel.VetViewModelFactory

// Grafos de cada perfil
private const val GRAFO_DUENO = "grafo_dueno"
private const val GRAFO_VET = "grafo_vet"

// Navega al grafo del rol y limpia la pila (no se puede volver al login)
fun NavHostController.irAlInicio(rol: Rol) {
    val destino = if (rol == Rol.ADMIN) GRAFO_VET else GRAFO_DUENO
    navigate(destino) { popUpTo(0) { inclusive = true } }
}

@Composable
fun NavVet(factory: VetViewModelFactory, sesion: SesionViewModel) {
    val nav = rememberNavController()
    val oscuro by sesion.modoOscuro.collectAsState()
    val usuario by sesion.usuarioActual.collectAsState()
    val duenoId = usuario?.id ?: 0

    // La barra inferior solo aparece en las pestañas principales
    val entrada by nav.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route

    Scaffold(
        bottomBar = {
            when {
                itemsDueno.any { it.ruta == rutaActual } -> BarraNavegacion(itemsDueno, rutaActual, nav)
                itemsVet.any { it.ruta == rutaActual } -> BarraNavegacion(itemsVet, rutaActual, nav)
            }
        }
    ) { padding ->
        NavHost(navController = nav, startDestination = Rutas.SPLASH, modifier = Modifier.padding(padding)) {

            composable(Rutas.SPLASH) {
                SplashScreen(onTerminar = {
                    sesion.restaurarSesion { rol ->
                        if (rol == null) {
                            nav.navigate(Rutas.LOGIN) { popUpTo(0) { inclusive = true } }
                        } else {
                            nav.irAlInicio(rol)
                        }
                    }
                })
            }

            composable(Rutas.LOGIN) {
                LoginScreen(vm = sesion, oscuro = oscuro, onEntrar = { rol -> nav.irAlInicio(rol) })
            }

            // ==================== Grafo del dueño ====================
            navigation(startDestination = Rutas.INICIO, route = GRAFO_DUENO) {

                composable(Rutas.INICIO) {
                    val vm: InicioViewModel = viewModel(factory = factory)
                    InicioScreen(
                        vm = vm,
                        duenoId = duenoId,
                        nombre = usuario?.nombre ?: "",
                        onMascota = { /* Paso 8: ficha */ },
                        onAgregar = { /* Paso 8: formulario */ },
                        onPedirCita = { /* Paso 9: pedir cita */ }
                    )
                }

                composable(Rutas.MASCOTAS) {
                    val vm: InicioViewModel = viewModel(factory = factory)
                    MascotasScreen(
                        vm = vm,
                        duenoId = duenoId,
                        onMascota = { /* Paso 8: ficha */ },
                        onAgregar = { /* Paso 8: formulario */ }
                    )
                }

                // Temporales: se completan en los Pasos 10 y 13
                composable(Rutas.MIS_CITAS) { Text("Mis citas") }
                composable(Rutas.PERFIL) { Text("Perfil") }
            }

            // ==================== Grafo del veterinario ====================
            navigation(startDestination = Rutas.PANEL, route = GRAFO_VET) {

                // Temporal: se reemplaza en el Paso 11
                composable(Rutas.PANEL) { Text("Panel de ${usuario?.nombre}") }

                composable(Rutas.SERVICIOS) {
                    val vm: ServiciosViewModel = viewModel(factory = factory)
                    Scaffold(topBar = { AppTopBar("Servicios") }) { p ->
                        ServiciosScreen(vm = vm, onEditar = { }, modifier = Modifier.padding(p))
                    }
                }
            }
        }
    }
}
