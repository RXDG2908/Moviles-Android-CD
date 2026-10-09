package com.saludplus.citas.ui.screens.citas

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// Pantalla 12 - Detalle de cita (reto extra)
// TODO: mostrar los datos de Repositorio.obtenerCita(citaId).
// TODO: botón "Cancelar cita" con AlertDialog de confirmación;
//  al aceptar, Repositorio.cancelarCita (remove de la lista) y volver.
@Composable
fun DetalleCitaScreen(navController: NavController, citaId: Int) {
    PantallaEnConstruccion(
        titulo = "Detalle de cita",
        botones = listOf("Volver" to { navController.popBackStack() })
    )
}
