package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// Pantalla 9 - Cita agendada (faltante, obligatoria)
// TODO: diseñarla con el estilo de la app: mensaje de éxito y resumen de la cita
//  (Repositorio.obtenerCita(citaId)).
// TODO: botones "Ver mis citas" -> MIS_CITAS e "Ir al inicio" -> HOME.
@Composable
fun CitaExitosaScreen(navController: NavController, citaId: Int) {
    PantallaEnConstruccion(
        titulo = "Cita agendada",
        botones = listOf(
            "Ver mis citas" to { navController.navigate(Rutas.MIS_CITAS) },
            "Ir al inicio" to { navController.popBackStack(Rutas.HOME, inclusive = false) }
        )
    )
}
