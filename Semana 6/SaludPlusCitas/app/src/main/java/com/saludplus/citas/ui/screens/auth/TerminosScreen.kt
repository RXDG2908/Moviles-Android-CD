package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// Pantalla 15 - Términos y condiciones (reto extra)
// TODO: texto de términos con scroll (o mostrarlo en un AlertDialog).
@Composable
fun TerminosScreen(navController: NavController) {
    PantallaEnConstruccion(
        titulo = "Términos y condiciones",
        botones = listOf("Volver" to { navController.popBackStack() })
    )
}
