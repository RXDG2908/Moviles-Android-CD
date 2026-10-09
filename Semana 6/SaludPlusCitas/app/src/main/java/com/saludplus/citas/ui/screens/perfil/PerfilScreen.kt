package com.saludplus.citas.ui.screens.perfil

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

// Pantalla 11 - Perfil / Mis datos (faltante, obligatoria)
// TODO: mostrar los datos de Repositorio.usuarioActual.
// TODO: "Cerrar sesión": Repositorio.cerrarSesion() y volver a SPLASH limpiando el historial.
@Composable
fun PerfilScreen(navController: NavController) {
    PantallaEnConstruccion(
        titulo = "Perfil",
        botones = listOf(
            "Cerrar sesión" to {
                navController.navigate(Rutas.SPLASH) {
                    popUpTo(0)
                }
            }
        )
    )
}
