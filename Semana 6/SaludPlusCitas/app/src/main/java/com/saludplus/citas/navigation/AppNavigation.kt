package com.saludplus.citas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saludplus.citas.ui.screens.agendamiento.CitaExitosaScreen
import com.saludplus.citas.ui.screens.agendamiento.ConfirmarCitaScreen
import com.saludplus.citas.ui.screens.agendamiento.EspecialidadesScreen
import com.saludplus.citas.ui.screens.agendamiento.FechaHoraScreen
import com.saludplus.citas.ui.screens.agendamiento.MedicosScreen
import com.saludplus.citas.ui.screens.auth.LoginScreen
import com.saludplus.citas.ui.screens.auth.RegistroScreen
import com.saludplus.citas.ui.screens.auth.SplashScreen
import com.saludplus.citas.ui.screens.auth.TerminosScreen
import com.saludplus.citas.ui.screens.citas.DetalleCitaScreen
import com.saludplus.citas.ui.screens.citas.MisCitasScreen
import com.saludplus.citas.ui.screens.home.HomeScreen
import com.saludplus.citas.ui.screens.notificaciones.NotificacionesScreen
import com.saludplus.citas.ui.screens.perfil.PerfilScreen
import com.saludplus.citas.ui.screens.resultados.ResultadosScreen
import com.saludplus.citas.ui.screens.sedes.SedesScreen

// NavHost con las 15 pantallas. No modificar salvo para agregar pantallas nuevas.
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Rutas.SPLASH) {

        // Autenticación
        composable(Rutas.SPLASH) { SplashScreen(navController) }
        composable(Rutas.REGISTRO) { RegistroScreen(navController) }
        composable(
            route = Rutas.LOGIN,
            arguments = listOf(navArgument("telefono") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) { entry ->
            val telefono = entry.arguments?.getString("telefono") ?: ""
            LoginScreen(navController, telefono)
        }
        composable(Rutas.TERMINOS) { TerminosScreen(navController) }

        // Inicio y destinos de la barra inferior
        composable(Rutas.HOME) { HomeScreen(navController) }
        composable(Rutas.MIS_CITAS) { MisCitasScreen(navController) }
        composable(Rutas.RESULTADOS) { ResultadosScreen(navController) }
        composable(Rutas.PERFIL) { PerfilScreen(navController) }
        composable(Rutas.NOTIFICACIONES) { NotificacionesScreen(navController) }

        // Flujo de agendamiento: primero se elige la sede
        composable(
            route = Rutas.SEDES,
            arguments = listOf(
                navArgument("destino") {
                    type = NavType.StringType
                    defaultValue = Rutas.IR_A_ESPECIALIDADES
                },
                navArgument("id") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { entry ->
            val destino = entry.arguments?.getString("destino") ?: Rutas.IR_A_ESPECIALIDADES
            val id = entry.arguments?.getInt("id") ?: 0
            SedesScreen(navController, destino, id)
        }

        composable(Rutas.ESPECIALIDADES) { EspecialidadesScreen(navController) }

        composable(
            route = Rutas.MEDICOS,
            arguments = listOf(navArgument("especialidadId") { type = NavType.IntType })
        ) { entry ->
            val especialidadId = entry.arguments?.getInt("especialidadId") ?: 0
            MedicosScreen(navController, especialidadId)
        }

        composable(
            route = Rutas.FECHA_HORA,
            arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
        ) { entry ->
            val medicoId = entry.arguments?.getInt("medicoId") ?: 0
            FechaHoraScreen(navController, medicoId)
        }

        composable(
            route = Rutas.CONFIRMAR_CITA,
            arguments = listOf(
                navArgument("medicoId") { type = NavType.IntType },
                navArgument("fecha") { type = NavType.StringType },
                navArgument("hora") { type = NavType.StringType }
            )
        ) { entry ->
            val medicoId = entry.arguments?.getInt("medicoId") ?: 0
            val fecha = entry.arguments?.getString("fecha") ?: ""
            val hora = entry.arguments?.getString("hora") ?: ""
            ConfirmarCitaScreen(navController, medicoId, fecha, hora)
        }

        composable(
            route = Rutas.CITA_EXITOSA,
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { entry ->
            val citaId = entry.arguments?.getInt("citaId") ?: 0
            CitaExitosaScreen(navController, citaId)
        }

        composable(
            route = Rutas.DETALLE_CITA,
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { entry ->
            val citaId = entry.arguments?.getInt("citaId") ?: 0
            DetalleCitaScreen(navController, citaId)
        }
    }
}
