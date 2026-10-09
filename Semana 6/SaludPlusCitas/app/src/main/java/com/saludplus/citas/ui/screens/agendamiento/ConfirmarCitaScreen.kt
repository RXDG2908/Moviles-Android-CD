package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// Pantalla 7 - Confirmar cita (diseño)
// TODO: mostrar médico, fecha, hora, tipo de atención y dirección usando los 3 parámetros recibidos.
// TODO: campo "Motivo de consulta (opcional)".
// TODO: "Agendar cita": crear y guardar la cita con Repositorio.agendarCita
//  y navegar a CITA_EXITOSA con popUpTo(HOME) para borrar el flujo del historial.
@Composable
fun ConfirmarCitaScreen(navController: NavController, medicoId: Int, fecha: String, hora: String) {
    PantallaEnConstruccion(
        titulo = "Confirmar cita ($fecha $hora)",
        botones = listOf(
            "Agendar cita" to {
                navController.navigate(Rutas.citaExitosa(0)) {
                    popUpTo(Rutas.HOME)
                }
            }
        )
    )
}
