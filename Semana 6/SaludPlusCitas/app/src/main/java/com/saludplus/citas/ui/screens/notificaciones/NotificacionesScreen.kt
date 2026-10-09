package com.saludplus.citas.ui.screens.notificaciones

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// Pantalla 14 - Notificaciones (reto extra)
// TODO: armar las notificaciones con map sobre Repositorio.citasDelUsuario()
//  (ej. "Recuerda tu cita del 16/10 a las 09:00").
@Composable
fun NotificacionesScreen(navController: NavController) {
    PantallaEnConstruccion(
        titulo = "Notificaciones",
        botones = listOf("Volver" to { navController.popBackStack() })
    )
}
