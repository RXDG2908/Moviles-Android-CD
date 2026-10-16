package com.tecsup.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.dueno.SplashScreen
import com.tecsup.huellitasvet.ui.veterinario.ServiciosScreen
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel
import com.tecsup.huellitasvet.viewmodel.VetViewModelFactory

// [G] GUIADO — se completa en los Pasos 6 a 13
@Composable
fun NavVet(factory: VetViewModelFactory) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Rutas.SPLASH) {

        composable(Rutas.SPLASH) {
            SplashScreen(onTerminar = {
                // TODO (Paso 6): si hay perfil guardado ir a INICIO o PANEL; si no, a LOGIN
                nav.navigate(Rutas.SERVICIOS) { popUpTo(Rutas.SPLASH) { inclusive = true } }
            })
        }

        // EJEMPLO RESUELTO
        composable(Rutas.SERVICIOS) {
            val vm: ServiciosViewModel = viewModel(factory = factory)
            Scaffold(topBar = { AppTopBar("Servicios") }) { padding ->
                ServiciosScreen(
                    vm = vm,
                    onEditar = { /* TODO (Paso 13): nav.navigate(Rutas.formularioServicio(it)) */ },
                    modifier = Modifier.padding(padding)
                )
            }
        }

        // TODO (Paso 6): LOGIN
        // TODO (Pasos 7-10): grafo del dueño con NavigationBar (Inicio, Mascotas, Citas, Perfil)
        // TODO (Pasos 11-13): grafo del veterinario con NavigationBar (Panel, Agenda, Pacientes, Servicios, Ajustes)
    }
}
