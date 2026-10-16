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
import androidx.navigation.compose.rememberNavController
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.dueno.LoginScreen
import com.tecsup.huellitasvet.ui.dueno.SplashScreen
import com.tecsup.huellitasvet.ui.veterinario.ServiciosScreen
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel
import com.tecsup.huellitasvet.viewmodel.SesionViewModel
import com.tecsup.huellitasvet.viewmodel.VetViewModelFactory

// Navega a la pantalla principal del rol y limpia la pila (no se puede volver al login)
fun NavHostController.irAlInicio(rol: Rol) {
    val destino = if (rol == Rol.ADMIN) Rutas.PANEL else Rutas.INICIO
    navigate(destino) { popUpTo(0) { inclusive = true } }
}

@Composable
fun NavVet(factory: VetViewModelFactory, sesion: SesionViewModel) {
    val nav = rememberNavController()
    val oscuro by sesion.modoOscuro.collectAsState()
    val usuario by sesion.usuarioActual.collectAsState()

    Scaffold { padding ->
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

            // Temporal: se reemplaza en el Paso 7
            composable(Rutas.INICIO) { Text("Hola, ${usuario?.nombre}") }
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
