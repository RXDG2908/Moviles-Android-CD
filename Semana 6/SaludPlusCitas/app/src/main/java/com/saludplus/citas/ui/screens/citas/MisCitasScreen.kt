package com.saludplus.citas.ui.screens.citas

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// Pantalla 10 - Mis citas (faltante, obligatoria)
// TODO: LazyColumn con Repositorio.citasDelUsuario().
// TODO: si la lista está vacía, mostrar un mensaje ("Aún no tienes citas").
// TODO: al tocar una cita ir a DETALLE_CITA con su id (reto extra).
@Composable
fun MisCitasScreen(navController: NavController) {
    PantallaEnConstruccion(
        titulo = "Mis citas",
        botones = listOf(
            "Ver detalle de cita" to { navController.navigate(Rutas.detalleCita(0)) },
            "Volver" to { navController.popBackStack() }
        )
    )
}
