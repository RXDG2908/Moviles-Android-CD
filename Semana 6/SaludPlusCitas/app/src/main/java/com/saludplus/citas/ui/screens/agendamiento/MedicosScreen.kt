package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// Pantalla 5 - Médicos (diseño)
// TODO: usar el parámetro especialidadId: título con el nombre de la especialidad
//  (Repositorio.obtenerEspecialidad).
// TODO: LazyColumn con Repositorio.medicosPorEspecialidad / buscarMedicos
//  (filter + sortedByDescending por calificación).
// TODO: cada médico con cargo, calificación, reseñas y chip de disponibilidad.
// TODO: al tocar un médico ir a FECHA_HORA con su id.
@Composable
fun MedicosScreen(navController: NavController, especialidadId: Int) {
    PantallaEnConstruccion(
        titulo = "Médicos (especialidad $especialidadId)",
        botones = listOf(
            "Elegir médico 1" to { navController.navigate(Rutas.fechaHora(1)) }
        )
    )
}
