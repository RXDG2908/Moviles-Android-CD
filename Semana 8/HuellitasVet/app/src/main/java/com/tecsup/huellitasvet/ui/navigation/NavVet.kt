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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.dueno.FichaMascotaScreen
import com.tecsup.huellitasvet.ui.dueno.FormularioMascotaScreen
import com.tecsup.huellitasvet.ui.dueno.InicioScreen
import com.tecsup.huellitasvet.ui.dueno.LoginScreen
import com.tecsup.huellitasvet.ui.dueno.MascotasScreen
import com.tecsup.huellitasvet.ui.dueno.PedirCitaScreen
import com.tecsup.huellitasvet.ui.dueno.SplashScreen
import com.tecsup.huellitasvet.ui.veterinario.ServiciosScreen
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel
import com.tecsup.huellitasvet.viewmodel.PedirCitaViewModel
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
    val aceptaCitas by sesion.aceptarCitas.collectAsState()
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
                        onMascota = { nav.navigate(Rutas.fichaMascota(it)) },
                        onAgregar = { nav.navigate(Rutas.formularioMascota()) },
                        onPedirCita = { nav.navigate(Rutas.pedirCita()) }
                    )
                }

                composable(Rutas.MASCOTAS) {
                    val vm: InicioViewModel = viewModel(factory = factory)
                    MascotasScreen(
                        vm = vm,
                        duenoId = duenoId,
                        onMascota = { nav.navigate(Rutas.fichaMascota(it)) },
                        onAgregar = { nav.navigate(Rutas.formularioMascota()) }
                    )
                }

                composable(
                    Rutas.FORMULARIO_MASCOTA,
                    arguments = listOf(navArgument("id") { type = NavType.IntType; defaultValue = -1 })
                ) { entrada ->
                    val id = entrada.arguments?.getInt("id")?.takeIf { it > 0 }
                    val vm: MascotaViewModel = viewModel(factory = factory)
                    FormularioMascotaScreen(
                        vm = vm,
                        duenoId = duenoId,
                        mascotaId = id,
                        onListo = { nav.popBackStack() },
                        // Al eliminar, vuelve a la lista (la ficha ya no existe)
                        onEliminada = { nav.popBackStack(Rutas.FICHA_MASCOTA, inclusive = true) },
                        onAtras = { nav.popBackStack() }
                    )
                }

                composable(
                    Rutas.FICHA_MASCOTA,
                    arguments = listOf(navArgument("id") { type = NavType.IntType })
                ) { entrada ->
                    val id = entrada.arguments?.getInt("id") ?: 0
                    val vm: MascotaViewModel = viewModel(factory = factory)
                    FichaMascotaScreen(
                        vm = vm,
                        mascotaId = id,
                        esDueno = true,
                        onPedirCita = { nav.navigate(Rutas.pedirCita(it)) },
                        onEditar = { nav.navigate(Rutas.formularioMascota(it)) },
                        onAtras = { nav.popBackStack() }
                    )
                }

                composable(
                    Rutas.PEDIR_CITA,
                    arguments = listOf(navArgument("mascotaId") { type = NavType.IntType; defaultValue = -1 })
                ) { entrada ->
                    val mascotaId = entrada.arguments?.getInt("mascotaId")?.takeIf { it > 0 }
                    val vm: PedirCitaViewModel = viewModel(factory = factory)
                    PedirCitaScreen(
                        vm = vm,
                        duenoId = duenoId,
                        mascotaInicial = mascotaId,
                        aceptaCitas = aceptaCitas,
                        // Vuelve a "Mis citas" y saca Pedir cita de la pila
                        onCitaCreada = {
                            nav.navigate(Rutas.MIS_CITAS) {
                                popUpTo(Rutas.INICIO)
                                launchSingleTop = true
                            }
                        },
                        onAtras = { nav.popBackStack() }
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
