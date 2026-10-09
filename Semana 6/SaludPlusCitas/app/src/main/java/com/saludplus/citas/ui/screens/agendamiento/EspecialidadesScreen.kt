package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// Pantalla 4 - Especialidades (diseño)
// TODO: campo de búsqueda; la lista se filtra en tiempo real con
//  Repositorio.buscarEspecialidades(texto) (filter + contains).
// TODO: LazyColumn de especialidades; al tocar una ir a MEDICOS con su id.
@Composable
fun EspecialidadesScreen(navController: NavController) {
    PantallaEnConstruccion(
        titulo = "Especialidades",
        botones = listOf(
            "Elegir Medicina General" to { navController.navigate(Rutas.medicos(1)) }
        )
    )
}
