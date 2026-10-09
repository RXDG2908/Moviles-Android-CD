package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// Pantalla 1 - Splash (diseño)
// TODO: logo de la clínica, "Clínica SaludPlus", lema "Tu salud, nuestra prioridad" e imagen (Image, Column).
// TODO: botón "Comenzar" -> REGISTRO y enlace "Ya tengo una cuenta" -> LOGIN.
@Composable
fun SplashScreen(navController: NavController) {
    PantallaEnConstruccion(
        titulo = "Splash",
        botones = listOf(
            "Comenzar" to { navController.navigate(Rutas.REGISTRO) },
            "Ya tengo una cuenta" to { navController.navigate(Rutas.LOGIN) }
        )
    )
}
