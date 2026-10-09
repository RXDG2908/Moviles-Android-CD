package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// Pantalla 6 - Fecha y hora (diseño)
// TODO: tarjeta del médico (Repositorio.obtenerMedico(medicoId)).
// TODO: lista fija de días para elegir: Lun 15 a Vie 19 de "Setiembre 2026" (selección de un día).
// TODO: LazyVerticalGrid con Repositorio.horariosDisponibles(medicoId, fecha):
//  un horario reservado ya no aparece para ese médico y fecha.
// TODO: "Continuar" habilitado solo con día y hora elegidos -> CONFIRMAR_CITA
//  con medicoId, fecha y hora.
@Composable
fun FechaHoraScreen(navController: NavController, medicoId: Int) {
    PantallaEnConstruccion(
        titulo = "Fecha y hora (médico $medicoId)",
        botones = listOf(
            "Continuar" to {
                navController.navigate(Rutas.confirmarCita(medicoId, "2026-09-16", "09:30"))
            }
        )
    )
}
