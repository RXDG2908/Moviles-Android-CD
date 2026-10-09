package com.saludplus.citas.ui.screens.resultados

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// Pantalla 13 - Resultados (reto extra)
// TODO: crear un modelo propio (por ejemplo Resultado) y mostrar una lista fija.
@Composable
fun ResultadosScreen(navController: NavController) {
    PantallaEnConstruccion(
        titulo = "Resultados",
        botones = listOf("Volver" to { navController.popBackStack() })
    )
}
